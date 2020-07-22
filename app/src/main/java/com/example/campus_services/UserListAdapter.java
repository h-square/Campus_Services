package com.example.campus_services;

import android.content.Context;
import android.service.autofill.UserData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListAdapterViewHolder> {

    public ArrayList<String> mUserName;
    public ArrayList<String> mUserType;
    public ArrayList<String> mUserUid;

    final private UserListAdapterOnClickHandler mClickHandler;

    public UserListAdapter(UserListAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public interface UserListAdapterOnClickHandler{
        void onClick(String[] currentUser);
    }

    public class UserListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            public final TextView vULI_User_Name;
            public final TextView vULI_User_Type;

        public UserListAdapterViewHolder(View view) {
            super(view);
            vULI_User_Name = (TextView) view.findViewById(R.id.ULI_User_Name);
            vULI_User_Type = (TextView) view.findViewById(R.id.ULI_User_Type);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String[] currentUser = new String[3];
            currentUser[0] = mUserName.get(adapterPosition);
            currentUser[1] = mUserType.get(adapterPosition);
            currentUser[2] = mUserUid.get(adapterPosition);
            mClickHandler.onClick(currentUser);
        }
    }

    @NonNull
    @Override
    public UserListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.userlist_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,parent,shouldAttachToParentImmediately);
        return new UserListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapterViewHolder userListAdapterViewHolder, int position) {
        String[] currentUserThis = new String[2];
        currentUserThis[0] = mUserName.get(position);
        currentUserThis[1] = mUserType.get(position);
        userListAdapterViewHolder.vULI_User_Name.setText(currentUserThis[0]);
        userListAdapterViewHolder.vULI_User_Type.setText(currentUserThis[1]);
    }

    @Override
    public int getItemCount() {
        if(mUserType == null) return 0;
        return mUserName.size();
    }

    public void setUserData(ArrayList<String> userName,ArrayList<String> userType,ArrayList<String> uid){
        mUserName = userName;
        mUserType = userType;
        mUserUid = uid;
        notifyDataSetChanged();
    }
}
