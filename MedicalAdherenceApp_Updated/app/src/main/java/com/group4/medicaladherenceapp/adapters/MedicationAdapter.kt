package com.group4.medicaladherenceapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.group4.medicaladherenceapp.R
import com.group4.medicaladherenceapp.models.Medication

class MedicationAdapter(
    private val medicationList: List<Medication>,
    private val onEditClick: (Medication) -> Unit,
    private val onDeleteClick: (Medication) -> Unit,
    private val onDoseClick: (Medication) -> Unit
) : RecyclerView.Adapter<MedicationAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtName: TextView = itemView.findViewById(R.id.txtMedicationName)
        val txtDosage: TextView = itemView.findViewById(R.id.txtDosage)
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val btnEdit: Button = itemView.findViewById(R.id.btnEditMedication)
        val btnDelete: Button = itemView.findViewById(R.id.btnDeleteMedication)
        val btnDose: Button = itemView.findViewById(R.id.btnRecordDose)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_medication, parent, false))
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medication = medicationList[position]
        holder.txtName.text = medication.name
        holder.txtDosage.text = "Dosage: ${medication.dosage}"
        holder.txtTime.text = "Time: ${medication.time}"
        holder.btnEdit.setOnClickListener { onEditClick(medication) }
        holder.btnDelete.setOnClickListener { onDeleteClick(medication) }
        holder.btnDose.setOnClickListener { onDoseClick(medication) }
    }
    override fun getItemCount(): Int = medicationList.size
}
