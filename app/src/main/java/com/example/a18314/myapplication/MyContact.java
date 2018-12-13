package com.example.a18314.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.jauker.widget.BadgeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyContact extends Fragment implements EMMessageListener{
    TextView dot;
    int position1;
    private ListView list;
    private HashMap<String, String> data = new HashMap<>();
    private ArrayList<String> imagelist = new ArrayList<>();
    private ArrayList<String> titlelist = new ArrayList<>();
    private HashMap<String, TextView> textViewArrayList = new HashMap<>();
    private BadgeView badgeView;
    private MyListadapter1 myListadapter;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_main,null);
        list=view.findViewById(R.id.ConversationList);
        badgeView=new BadgeView(getActivity());
        context=getActivity();
        myListadapter=new MyListadapter1(context,dot,imagelist,titlelist,textViewArrayList,badgeView);
        imagelist.add("http://img03.store.sogou.com/app/a/10010016/b1296a6eecf78db4143f93678c40ed0b");
        imagelist.add("http://img02.store.sogou.com/app/a/10010016/c150433fde62388a34a54a78eeba5dad");
        imagelist.add("https://img01.sogoucdn.com/app/a/100520093/ea54b1c5225b5b8f-1f7d693ce5c84217-65bcb52a88b749bfa5bd038796a7f699.jpg");
        imagelist.add("https://img02.sogoucdn.com/app/a/100520093/ea54b1c5225b5b8f-1f7d693ce5c84217-1d1d19aae89d869ae9896c9596b3e545.jpg");
        imagelist.add("http://img02.store.sogou.com/app/a/10010016/ec7c36fea05c415dc2f01ab6c279313c");
        imagelist.add("https://img03.sogoucdn.com/app/a/100520093/b9575637bcbb74a7-c2b6eb5dcccf21ec-8c2bab625db3b51cab9c4bc6241b042a.jpg");
        titlelist.add("杜宇航");
        titlelist.add("高笑然");
        titlelist.add("刘耘汉");
        titlelist.add("王志勇");
        titlelist.add("fff");
        titlelist.add("ggg");
        data.put("fff", "http://img02.store.sogou.com/app/a/10010016/ec7c36fea05c415dc2f01ab6c279313c");
        data.put("ggg", "https://img03.sogoucdn.com/app/a/100520093/b9575637bcbb74a7-c2b6eb5dcccf21ec-8c2bab625db3b51cab9c4bc6241b042a.jpg");
        final String username = getActivity().getIntent().getStringExtra("username");
        list.setAdapter(myListadapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ChatActivity.class);
                intent.putExtra("image", imagelist.get(position));
                intent.putExtra("title", titlelist.get(position));
                intent.putExtra("sendusername", username);
                intent.putExtra("sendimage", data.get(username));
                startActivity(intent);
                badgeView.setVisibility(View.GONE);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                registerForContextMenu(list);
                position1=position;
                return false;
            }
        });
        return view;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.item,menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        imagelist.remove(position1);
        titlelist.remove(position1);
        myListadapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }
    @Override
    public void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    @Override
    public void onMessageReceived(List<EMMessage> list) {
//        final String s = list.get(0).getFrom().toString();
//        Log.i("TAG",s);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean b = Looper.getMainLooper() == Looper.myLooper();
                Log.i("TAG",b+"");
//                badgeView.setTargetView(textViewArrayList.get(s));
//                badgeView.setBadgeCount(1);
                TranslateAnimation animation = new TranslateAnimation(0, -5, 0, 0);
                animation.setDuration(100);
                animation.setRepeatCount(3);
                badgeView.startAnimation(animation);
                myListadapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {

    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }

    @Override
    public void onMessageRecalled(List<EMMessage> list) {

    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }
}
class MyListadapter1 extends BaseAdapter {
    SimpleDraweeView image;
    TextView title;

    private Context context;
    private TextView dot;
    private ArrayList<String> imagelist;
    private ArrayList<String> titlelist;
    private HashMap<String, TextView> textViewArrayList;
    private BadgeView badgeView;

    public MyListadapter1(Context context, TextView dot, ArrayList<String> imagelist, ArrayList<String> titlelist, HashMap<String, TextView> textViewArrayList, BadgeView badgeView) {
        this.context = context;
        this.dot = dot;
        this.imagelist = imagelist;
        this.titlelist = titlelist;
        this.textViewArrayList = textViewArrayList;
        this.badgeView = badgeView;
    }

    @Override
    public int getCount() {
        return imagelist.size();
    }

    @Override
    public Object getItem(int position) {
        return imagelist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item, null);
        image = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        dot = view.findViewById(R.id.dot);
        image.setImageURI(Uri.parse(imagelist.get(position)));
        title.setText(titlelist.get(position));
        textViewArrayList.put(titlelist.get(position), dot);
        try {
            EMConversation conversation = EMClient.getInstance().chatManager().getConversation(titlelist.get(position));
            int count = conversation.getUnreadMsgCount();
            if (count > 0) {
                badgeView.setTargetView(textViewArrayList.get(titlelist.get(position)));
                badgeView.setBadgeCount(count);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}