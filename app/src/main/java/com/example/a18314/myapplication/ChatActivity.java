package com.example.a18314.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.github.library.bubbleview.BubbleTextView;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMLocationMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.ui.EaseBaiduMapActivity;
import com.hyphenate.easeui.widget.EaseChatExtendMenu;
import com.hyphenate.easeui.widget.EaseChatInputMenu;
import com.hyphenate.easeui.widget.EaseChatMessageList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener, EMMessageListener {
    private EaseChatInputMenu inputMenu;
    private ListView listMessage;
    String url;
    String image;
    String username;
    String titlename;
    private ImageView finish;
    private TextView title;
    private EditText edtitext;
    final Myadapter myadapter = new Myadapter();
    private HashMap<String, String> data = new HashMap<>();
    private HashMap<String, String> data1 = new HashMap<>();
    private ArrayList<EMMessage> datalist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initview();
    }

    private void initview() {
        inputMenu = (EaseChatInputMenu) findViewById(R.id.input_menu);
        listMessage = (ListView) findViewById(R.id.list_message);
        finish = (ImageView) findViewById(R.id.finish);
        title = (TextView) findViewById(R.id.chat_title);

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titlename = getIntent().getStringExtra("title");
        title.setText(titlename);
        url = getIntent().getStringExtra("image");
        data.put(titlename, url);
        username = getIntent().getStringExtra("sendusername");
        image = getIntent().getStringExtra("sendimage");
        data1.put(username, image);
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show();
        Log.d("Myadapter", data1.get(username));
        listMessage.setAdapter(new Myadapter());
        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.common_full_open_on_phone, 1, this);
        inputMenu.registerExtendMenuItem(R.string.attach_picture, R.drawable.common_full_open_on_phone, 2, this);
        inputMenu.registerExtendMenuItem(R.string.attach_location, R.drawable.common_full_open_on_phone, 3, this);
        //初始化，此操作需放在registerExtendMenuItem后
        inputMenu.init();
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {
            @Override
            public void onTyping(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onSendMessage(String content) {
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createTxtSendMessage(content, titlename);
                //如果是群聊，设置chattype，默认是单聊
                message.setChatType(EMMessage.ChatType.Chat);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                message.setMessageStatusCallback(new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.d("ChatMainActivity", "消息发送成功");
                    }

                    @Override
                    public void onError(int i, String s) {
                        Log.d("ChatMainActivity", "消息发送失败," + i + "," + s);
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                datalist.add(message);
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {

            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    @Override
    public void onClick(int itemId, View view) {
        switch (itemId) {
            case 1:
                Toast.makeText(this, "1", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,1);
                break;
            case 3:
                Intent intent1 = new Intent(ChatActivity.this, EaseBaiduMapActivity.class);
                startActivityForResult(intent1,2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if (requestCode==1&&resultCode== Activity.RESULT_OK){
            ContentResolver resolver=getContentResolver();
            Cursor cursor = resolver.query(uri, null, null, null, null);
            while(cursor.moveToNext()){
                String data2 = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Toast.makeText(this, data2, Toast.LENGTH_SHORT).show();
                //创建一条文本消息，content为消息文字内容，toChatUsername为对方用户或者群聊的id，后文皆是如此
                EMMessage message = EMMessage.createImageSendMessage(data2,false,titlename);
                //如果是群聊，设置chattype，默认是单聊
                message.setChatType(EMMessage.ChatType.Chat);
                //发送消息
                EMClient.getInstance().chatManager().sendMessage(message);
                datalist.add(message);
                myadapter.notifyDataSetChanged();
            }
        }else if (requestCode==2&&resultCode== Activity.RESULT_OK){

        }

    }

    @Override
    public void onMessageReceived(final List<EMMessage> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (final EMMessage message : list) {
                    if (message.getType().equals(EMMessage.Type.TXT)){
                        datalist.add(message);
                        myadapter.notifyDataSetChanged();
                    } else if (message.getType().equals(EMMessage.Type.IMAGE)){
                        datalist.add(message);
                        myadapter.notifyDataSetChanged();
                    }

                }
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

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EMClient.getInstance().chatManager().removeMessageListener(this);
    }

    class Myadapter extends BaseAdapter {
        BubbleTextView content1;
        SimpleDraweeView image1;
        BubbleTextView content2;
        SimpleDraweeView image2;

        @Override
        public int getCount() {
            return datalist.size();
        }

        @Override
        public Object getItem(int position) {
            return datalist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(ChatActivity.this).inflate(R.layout.item1, null);
            content1 = view.findViewById(R.id.tv1);
            image1 = view.findViewById(R.id.image1);
            content2 = view.findViewById(R.id.tv2);
            image2 = view.findViewById(R.id.image2);
            EMMessage emMessage = datalist.get(position);
            if (emMessage.direct().ordinal() == 0) {
                Toast.makeText(ChatActivity.this, "我是发送方", Toast.LENGTH_SHORT).show();
//                content2.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
//                content2.setVisibility(View.VISIBLE);
//                image2.setImageURI(Uri.parse(data1.get(username)));
//                image2.setVisibility(View.VISIBLE);
                //            满足该条件则是发送方
                if (emMessage.getType().equals(EMMessage.Type.TXT)) {
                    image2.setImageURI(Uri.parse(data1.get(username)));
                    content2.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
                    image2.setVisibility(View.VISIBLE);
                    content2.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(ChatActivity.this, "我是接受方", Toast.LENGTH_SHORT).show();
//                content1.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
//                content1.setVisibility(View.VISIBLE);
//                image1.setImageURI(Uri.parse(data.get(titlename)));
//                image1.setVisibility(View.VISIBLE);
                if (emMessage.getType().equals(EMMessage.Type.TXT)) {
                    image1.setImageURI(Uri.parse(data.get(titlename)));
                    content1.setText(((EMTextMessageBody) emMessage.getBody()).getMessage());
                    image1.setVisibility(View.VISIBLE);
                    content1.setVisibility(View.VISIBLE);
                } else if (emMessage.getType().equals(EMMessage.Type.IMAGE)) {
                    Log.i("Mychatlistadapter", ((EMImageMessageBody) emMessage.getBody()).getThumbnailUrl());
                    image1.setImageURI(Uri.parse(((EMImageMessageBody) emMessage.getBody()).getThumbnailUrl()));
                    image1.setVisibility(View.VISIBLE);
                }
            }
            return view;
        }


    }
}
