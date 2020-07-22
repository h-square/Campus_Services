package com.example.campus_services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AppointmentListAdapter extends RecyclerView.Adapter<AppointmentListAdapter.AppointmentListAdapterViewHolder> {

    public ArrayList<Appointment> mAppointments;

    public class AppointmentListAdapterViewHolder extends RecyclerView.ViewHolder {
            public final TextView vALI_Name,vALI_Date,vALI_Time,vALI_Slot;

        public AppointmentListAdapterViewHolder(View view) {
            super(view);
            vALI_Name = (TextView) view.findViewById(R.id.ALI_Name);
            vALI_Date = (TextView) view.findViewById(R.id.ALI_Date);
            vALI_Time = (TextView) view.findViewById(R.id.ALI_Time);
            vALI_Slot = (TextView) view.findViewById(R.id.ALI_Slot);
        }

    }

    @NonNull
    @Override
    public AppointmentListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.appointment_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new AppointmentListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentListAdapterViewHolder appointmentListAdapterViewHolder, int position) {
        String name = mAppointments.get(position).getStudent_Id();
        String date = mAppointments.get(position).getDate();
        String time = CalendarUtils.convertTimeFromSectoHHMM(mAppointments.get(position).getTime());
        String slot = "Slot-" + String.valueOf(mAppointments.get(position).getSlot());

        appointmentListAdapterViewHolder.vALI_Name.setText(name);
        appointmentListAdapterViewHolder.vALI_Date.setText(date);
        appointmentListAdapterViewHolder.vALI_Time.setText(time);
        appointmentListAdapterViewHolder.vALI_Slot.setText(slot);
    }

    @Override
    public int getItemCount() {
        if(mAppointments == null) return 0;
        return mAppointments.size();
    }


    public void setAppointmentData(ArrayList<Appointment> appointmentData){
        mAppointments = appointmentData;
        notifyDataSetChanged();
    }
}
