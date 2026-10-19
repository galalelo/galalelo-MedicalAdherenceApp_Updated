import { initializeApp } from "https://www.gstatic.com/firebasejs/13.0.0/firebase-app.js";
import {
  getAuth, onAuthStateChanged, createUserWithEmailAndPassword,
  signInWithEmailAndPassword, GoogleAuthProvider, signInWithPopup,
  sendPasswordResetEmail, signOut, updateProfile
} from "https://www.gstatic.com/firebasejs/13.0.0/firebase-auth.js";
import {
  getFirestore, doc, getDoc, setDoc, updateDoc, collection, getDocs,
  addDoc, query, orderBy, writeBatch, deleteDoc
} from "https://www.gstatic.com/firebasejs/13.0.0/firebase-firestore.js";

// Public Firebase Web app configuration for the same project used by Android.
const firebaseConfig = {
  apiKey: "AIzaSyB0U3HNTXyBrMAj2eiwABhL3k_vnoSfSNY",
  authDomain: "medical-adherence-android-app.firebaseapp.com",
  projectId: "medical-adherence-android-app",
  storageBucket: "medical-adherence-android-app.firebasestorage.app",
  messagingSenderId: "404058041372",
  appId: "1:404058041372:web:55b5fb6376d77ae69903d9",
  measurementId: "G-98EBK5H855"
};

const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const db = getFirestore(app);
const page = location.pathname.split('/').pop() || 'index.php';
const delegatedMedicationHandlers = new WeakSet();
const privatePages = new Set([
  'dashboard.php','profile.php','edit_profile.php','about_account.php','medications.php',
  'add_medication.php','edit_medication.php','notifications.php','about.php','app_info.php',
  'developer_info.php','privacy_policy.php','terms_of_use.php'
]);
const userDoc = uid => doc(db, 'users', uid);
const nested = (uid, name) => collection(db, 'users', uid, name);
const localDate = (date = new Date()) => `${date.getFullYear()}-${String(date.getMonth()+1).padStart(2,'0')}-${String(date.getDate()).padStart(2,'0')}`;
const notify = (message, type = 'success') => {
  let el = document.querySelector('.toast.firebase-toast');
  if (!el) { el = document.createElement('div'); el.className = 'toast firebase-toast'; document.querySelector('.phone-shell')?.prepend(el); }
  el.textContent = message; el.classList.toggle('error', type === 'error'); el.classList.remove('hide');
  setTimeout(() => el.classList.add('hide'), 4000);
};
const friendlyError = error => {
  const code = error?.code || '';
  const known = {
    'auth/email-already-in-use':'That email already has an account.',
    'auth/invalid-credential':'Email or password is incorrect.',
    'auth/invalid-email':'Enter a valid email address.',
    'auth/weak-password':'Use a stronger password (at least 6 characters).',
    'auth/popup-closed-by-user':'Google sign-in was closed before completing.',
    'permission-denied':'Firebase denied access. Check Firestore rules allow this signed-in user to access only their own user document.'
  };
  return known[code] || error?.message || 'Something went wrong. Please try again.';
};
const profileFor = user => getDoc(userDoc(user.uid)).then(s => s.exists() ? s.data() : ({
  uid:user.uid, fullName:user.displayName || '', email:user.email || '', phone:'', medicalCondition:''
}));
const html = value => String(value ?? '').replace(/[&<>"']/g, c => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));
const dateLabel = value => {
  const date = typeof value === 'number' ? new Date(value) : value?.toDate ? value.toDate() : new Date(value || Date.now());
  return isNaN(date.getTime()) ? '' : date.toLocaleString();
};

async function renderUser(user) {
  const name = user.displayName || (await profileFor(user)).fullName || user.email || 'there';
  document.querySelectorAll('[data-firebase-name]').forEach(el => { el.textContent = name; });
  if (page === 'dashboard.php') {
    const meds = await getDocs(nested(user.uid, 'medications'));
    const count = document.querySelector('[data-medication-count]');
    if (count) count.textContent = String(meds.size);
  }
  if (page === 'profile.php' || page === 'about_account.php') {
    const profile = await profileFor(user);
    for (const key of ['fullName','email','phone','medicalCondition']) {
      const target = document.querySelector(`[data-profile="${key}"]`);
      if (target) target.textContent = profile[key] || (key === 'phone' || key === 'medicalCondition' ? 'Not provided' : '');
    }
  }
  if (page === 'edit_profile.php') {
    const profile = await profileFor(user);
    for (const [field,key] of [['name','fullName'],['email','email'],['phone','phone'],['condition','medicalCondition']]) {
      const input = document.querySelector(`[name="${field}"]`);
      if (input) input.value = profile[key] || user.email || '';
    }
  }
  if (page === 'medications.php') await renderMedications(user);
  if (page === 'edit_medication.php') await loadMedicationForm(user);
  if (page === 'notifications.php') await renderNotifications(user);
  startReminderChecks(user);
}

async function renderMedications(user) {
  const container = document.querySelector('[data-medication-list]');
  if (!container) return;
  const medsSnap = await getDocs(nested(user.uid, 'medications'));
  const meds = medsSnap.docs.map(d => ({id:d.id,...d.data()})).sort((a,b)=>(b.createdAt||0)-(a.createdAt||0));
  const today = localDate();
  const adherenceSnap = await getDocs(query(nested(user.uid,'adherence'), orderBy('date','desc'))).catch(() => getDocs(nested(user.uid,'adherence')));
  const takenIds = new Set(adherenceSnap.docs.map(d=>d.data()).filter(x=>x.date===today && String(x.status).toLowerCase()==='taken').map(x=>x.medicationId));
  container.replaceChildren();
  if (!meds.length) { container.innerHTML='<div class="empty-state"><h3>No medications yet</h3><p>Add your first medication to see it listed here.</p><a class="btn inline" href="add_medication.php">Add Medication</a></div>'; return; }
  for (const med of meds) {
    const isTaken = takenIds.has(med.id) || takenIds.has(med.medicationId);
    const card = document.createElement('article'); card.className='list-card medication-card';
    card.innerHTML=`<div class="medication-card-heading"><div class="medication-icon" aria-hidden="true">+</div><div class="medication-name"><h3>${html(med.medicationName)}</h3><span class="medication-label">Scheduled medication</span></div></div>
      <div class="medication-details"><div class="medication-detail"><span>Dosage</span><strong>${html(med.dosage)}</strong></div><div class="medication-detail"><span>Scheduled time</span><strong>${html(String(med.medicationTime||'').slice(0,5))}</strong></div></div>
      <div class="medication-status-row"><span>Today's dose</span><span class="medication-status ${isTaken?'taken':'pending'}">${isTaken?'Taken':'Pending'}</span></div>
      <div class="card-actions medication-actions">${isTaken?'':`<button class="btn small" type="button" data-take="${html(med.id)}">Mark as Taken</button>`}<a class="btn small accent" href="edit_medication.php?id=${encodeURIComponent(med.id)}">Edit</a><button class="btn small danger" type="button" data-delete="${html(med.id)}">Delete</button></div>`;
    container.append(card);
  }
  if (!delegatedMedicationHandlers.has(container)) container.addEventListener('click', async event => {
    const take = event.target.closest('[data-take]'); const del = event.target.closest('[data-delete]');
    try {
      if (take) {
        const id=take.dataset.take;
        await addDoc(nested(user.uid,'adherence'),{medicationId:id,status:'Taken',date:today,loggedAt:Date.now()});
        notify('Dose marked as taken.'); await renderMedications(user);
      }
      if (del && confirm('Delete this medication?')) {
        await deleteDoc(doc(db,'users',user.uid,'medications',del.dataset.delete)); notify('Medication deleted.'); await renderMedications(user);
      }
    } catch(error) { notify(friendlyError(error),'error'); }
  });
  delegatedMedicationHandlers.add(container);
}

async function loadMedicationForm(user) {
  const form=document.querySelector('[data-medication-form]'); if(!form)return;
  const id=new URLSearchParams(location.search).get('id');
  if (!id) { notify('Medication not found.','error'); return; }
  const snap=await getDoc(doc(db,'users',user.uid,'medications',id));
  if (!snap.exists()) { notify('Medication not found.','error'); return; }
  const med=snap.data();
  for (const [field,key] of [['medicationName','medicationName'],['dosage','dosage'],['medicationTime','medicationTime']]) { const input=form.elements.namedItem(field); if(input)input.value=med[key]||''; }
}

async function renderNotifications(user) {
  const container=document.querySelector('[data-notification-list]'); if(!container)return;
  const snap=await getDocs(query(nested(user.uid,'notifications'),orderBy('notificationDate','desc'))).catch(()=>getDocs(nested(user.uid,'notifications')));
  const items=snap.docs.map(d=>d.data()).sort((a,b)=>(b.notificationDate||0)-(a.notificationDate||0));
  container.replaceChildren();
  if(!items.length){container.innerHTML='<div class="empty-state"><h3>No notifications yet</h3><p>Your medication reminders and updates will appear here.</p></div>';return;}
  const list=document.createElement('div');list.className='notifications-list';
  for(const item of items){const card=document.createElement('article');card.className='info-card notification-card';card.innerHTML=`<div class="notification-icon" aria-hidden="true"><span>!</span></div><div class="notification-content"><div class="notification-card-heading"><h3>${html(item.title)}</h3><span class="notification-date">${html(dateLabel(item.notificationDate))}</span></div><p>${html(item.message)}</p></div>`;list.append(card);} container.append(list);
}

function bindForms(user) {
  const login=document.querySelector('[data-firebase-login]');
  login?.addEventListener('submit',async e=>{e.preventDefault();try{await signInWithEmailAndPassword(auth,login.elements.email.value.trim(),login.elements.password.value);location.href='dashboard.php';}catch(err){notify(friendlyError(err),'error');}});
  document.querySelector('[data-google-login]')?.addEventListener('click',async()=>{window.__signingIn=true;try{
    const u=(await signInWithPopup(auth,new GoogleAuthProvider())).user;
    // Create the shared profile only if it does not exist yet, so existing phone/condition are never overwritten.
    const ref=userDoc(u.uid);
    if(!(await getDoc(ref)).exists())await setDoc(ref,{uid:u.uid,fullName:u.displayName||'',email:u.email||'',phone:u.phoneNumber||'',medicalCondition:'',createdAt:Date.now()},{merge:true});
    location.href='dashboard.php';
  }catch(err){window.__signingIn=false;notify(friendlyError(err),'error');}});
  const register=document.querySelector('[data-firebase-register]');
  register?.addEventListener('submit',async e=>{e.preventDefault();window.__signingIn=true;try{
    const name=register.elements.name.value.trim(), email=register.elements.email.value.trim(), password=register.elements.password.value;
    if(!name)throw new Error('Please enter your full name.');
    const cred=await createUserWithEmailAndPassword(auth,email,password); await updateProfile(cred.user,{displayName:name});
    await setDoc(userDoc(cred.user.uid),{uid:cred.user.uid,fullName:name,email:cred.user.email,phone:register.elements.phone.value.trim(),medicalCondition:register.elements.condition.value.trim(),createdAt:Date.now()},{merge:true}); location.href='dashboard.php';
  }catch(err){window.__signingIn=false;notify(friendlyError(err),'error');}});
  const reset=document.querySelector('[data-firebase-reset]'); reset?.addEventListener('submit',async e=>{e.preventDefault();try{await sendPasswordResetEmail(auth,reset.elements.email.value.trim());notify('Password reset email sent.');}catch(err){notify(friendlyError(err),'error');}});
  document.querySelectorAll('[data-firebase-logout]').forEach(el=>el.addEventListener('click',async e=>{e.preventDefault();await signOut(auth);location.href='login.php';}));
  const add=document.querySelector('[data-medication-form]');
  add?.addEventListener('submit',async e=>{e.preventDefault();try{
    const name=add.elements.medicationName.value.trim(), dosage=add.elements.dosage.value.trim(), time=add.elements.medicationTime.value;
    if(!name||!dosage||!time)throw new Error('Please complete all fields.');
    const editing=new URLSearchParams(location.search).get('id');
    if(editing){await updateDoc(doc(db,'users',user.uid,'medications',editing),{medicationName:name,dosage,medicationTime:time});}
    else {const meds=nested(user.uid,'medications');const ref=doc(meds);const batch=writeBatch(db);batch.set(ref,{medicationId:ref.id,medicationName:name,dosage,medicationTime:time,status:'Pending',frequency:'Daily',instructions:'',createdAt:Date.now()});batch.set(doc(nested(user.uid,'notifications')),{title:'Medication added',message:`${name} was added to your medication list`,notificationDate:Date.now(),read:false});await batch.commit();}
    location.href='medications.php';
  }catch(err){notify(friendlyError(err),'error');}});
  const profile=document.querySelector('[data-firebase-profile]');profile?.addEventListener('submit',async e=>{e.preventDefault();try{const fullName=profile.elements.name.value.trim();if(!fullName)throw new Error('Please enter your full name.');await setDoc(userDoc(user.uid),{uid:user.uid,fullName,email:user.email||'',phone:profile.elements.phone.value.trim(),medicalCondition:profile.elements.condition.value.trim()},{merge:true});if(auth.currentUser?.displayName!==fullName)await updateProfile(auth.currentUser,{displayName:fullName});location.href='profile.php';}catch(err){notify(friendlyError(err),'error');}});
}

function startReminderChecks(user) {
  if(!('Notification' in window) || window.__firebaseReminderLoop) return;
  window.__firebaseReminderLoop=true;
  const notified=new Set();
  const check=async()=>{if(Notification.permission!=='granted')return;try{
    const snap=await getDocs(nested(user.uid,'medications'));const now=new Date();const day=localDate(now);const hm=now.toTimeString().slice(0,5);
    for(const d of snap.docs){const med=d.data();if(String(med.medicationTime||'').slice(0,5)!==hm)continue;const key=`${user.uid}-${d.id}-${day}-${hm}`;if(notified.has(key)||localStorage.getItem(`reminder:${key}`))continue;
      new Notification('Medication Reminder',{body:`It is time to take ${med.medicationName} (${med.dosage}).`});
      await setDoc(doc(db,'users',user.uid,'notifications',`reminder_${d.id}_${day}_${hm.replace(':','')}`),{title:'Medication Reminder',message:`It is time to take ${med.medicationName} (${med.dosage}).`,notificationDate:Date.now(),read:false},{merge:true});
      notified.add(key);localStorage.setItem(`reminder:${key}`,'1');
    }
  }catch(err){console.warn('Firebase reminder check:',friendlyError(err));}};
  if(Notification.permission==='default')document.addEventListener('click',()=>Notification.requestPermission().then(p=>{if(p==='granted')check();}),{once:true});
  check();window.setInterval(check,30000);
}

onAuthStateChanged(auth, async user => {
  const isAuthPage=['login.php','register.php','forgot_password.php','index.php',''].includes(page);
  if(user){
    document.querySelectorAll('[data-auth-only]').forEach(el=>el.hidden=false);
    if(isAuthPage){if(!window.__signingIn)location.replace('dashboard.php');return;}
    await renderUser(user);bindForms(user);
  }else{
    document.querySelectorAll('[data-auth-only]').forEach(el=>el.hidden=true);
    if(privatePages.has(page))location.replace('login.php');
    bindForms(null);
  }
});
