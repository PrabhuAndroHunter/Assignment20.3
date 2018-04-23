package com.assignment.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.assignment.MainActivity;
import com.assignment.R;
import com.assignment.model.Contact;
import com.assignment.util.ContactHelper;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter <ContactAdapter.MyViewHolder> {
    final String TAG = ContactAdapter.class.toString();
    MainActivity mainActivity;
    List <Contact> contactList = new ArrayList <Contact>();

    public ContactAdapter(Context context) {
        mainActivity = (MainActivity) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Contact contactObj = contactList.get(position);

        holder.mNameTv.setText(contactObj.getName());
        holder.optionLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(mainActivity, holder.optionLine, Gravity.END);
                popup.getMenuInflater().inflate(R.menu.menu_contact, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        deleteContact(contactObj.getPhoneNumber());
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mNameTv;
        protected AppCompatImageView optionLine;

        public MyViewHolder(View view) {
            super(view);
            mNameTv = (TextView) view.findViewById(R.id.text_view_name);
            optionLine = (AppCompatImageView) view.findViewById(R.id.image_button_action);
        }
    }

    public void refreshUI(List <Contact> contactList) {
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    private void deleteContact(String phoneNumber) {
        int result = ContactHelper.deleteContact(mainActivity.getContentResolver(), phoneNumber);
        if (result == 1) {
            Toast.makeText(mainActivity, "Contact Deleted", Toast.LENGTH_SHORT).show();
        }
    }
}