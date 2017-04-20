package com.vnindie.glocoapp.doctor_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vnindie.glocoapp.R;
import com.vnindie.glocoapp.data.Doctor;

import java.util.List;

class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder> {
  private List<Doctor> mDoctors;

  DoctorAdapter(List<Doctor> doctors) {
    this.mDoctors = doctors;
  }

  @Override
  public DoctorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    return null;
  }

  @Override
  public void onBindViewHolder(DoctorViewHolder doctorViewHolder, int pos) {
    Doctor doc = mDoctors.get(pos);
    doctorViewHolder.bind(doc);
  }

  void updateData(List<Doctor> doctors) {
    this.mDoctors = doctors;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return mDoctors == null ? 0 : mDoctors.size();
  }

  static class DoctorViewHolder extends RecyclerView.ViewHolder {
    private TextView mDoctorName;

    public DoctorViewHolder(View itemView) {
      super(itemView);
      mDoctorName = (TextView) itemView.findViewById(R.id.doctorName);
    }

    void bind(Doctor doc) {
      mDoctorName.setText(doc.getName());
    }
  }
}
