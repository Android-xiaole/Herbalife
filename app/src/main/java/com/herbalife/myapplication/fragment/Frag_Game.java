package com.herbalife.myapplication.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.herbalife.myapplication.GameAdapter;
import com.herbalife.myapplication.ImageRes;
import com.herbalife.myapplication.MyApplication;
import com.herbalife.myapplication.R;
import com.herbalife.myapplication.Result2Popwindow;
import com.herbalife.myapplication.ResultPopwindow;
import com.herbalife.myapplication.UtilDate;
import com.herbalife.myapplication.camera.model.Addon;
import com.herbalife.myapplication.zxing.activity.Act_ZXing;
import com.sitsmice.herbalife_jar.base.BaseFragment;
import com.sitsmice.herbalife_jar.utils.UtilObjectIO;
import com.zhy.autolayout.AutoFrameLayout;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by sahara on 2016/11/15.
 */

public class Frag_Game extends BaseFragment{

    private GridView gridView;
    private Button button;
    private AutoFrameLayout frameLayout;
    private ImageView iv_logo;
    private TextView tv_score,tv_unlock6;

    private GameAdapter adapter;
    private List<ImageRes> list = new ArrayList<>();

    public Frag_Game(){
        r = R.layout.frag_game;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath);
        if (list!=null){
            this.list = list;
        }else{
            this.list.add(new ImageRes("1",300,R.drawable.img_game_map1_false,R.drawable.img_game_map_true,"餐厅","Restaurant","我们都是梦想家，同时我们也都是美食家，让我们来品味特别的异域餐厅美食吧。",false));
            this.list.add(new ImageRes("2",380,R.drawable.img_game_map2_false,R.drawable.img_game_map_true,"长尾船","Long Tail","在泰国当地，长尾船被称为最方便的交通工具。船尾尖尖，缀以大串鲜花乘风破浪在前，驾驶员的船头反在后面。",false));
            this.list.add(new ImageRes("3",500,R.drawable.img_game_map3_false,R.drawable.img_game_map_true,"King Power免税","King Power Duty-Free Store","在这里除了世界各国的大牌之外。当地特色产品：药膏精油、水果食品、在这里除了世界各国的大牌以外，当地特色产品：药膏精油、水果食品、曼谷包也是我们馈赠亲朋好友的优选礼品。",false));
            this.list.add(new ImageRes("4",660,R.drawable.img_game_map4_false,R.drawable.img_game_map_true,"普吉山顶","Popularize The Peak","登上观景台，居高临下可以俯瞰整个普吉和蔚蓝的大海。",false));
            this.list.add(new ImageRes("5",700,R.drawable.img_game_map5_false,R.drawable.img_game_map_true,"神仙半岛","Fairy Peninsula","突出于普吉岛最南端，泰语中德意思为上帝的岬角，景观台上供奉了四面佛，此处是傍晚看日落的最佳地方。",false));
            this.list.add(new ImageRes("6",780,R.drawable.img_game_map6_false,R.drawable.img_game_map_true,"晚宴","Dinner","这次我们为大家准备的晚宴可是大名鼎鼎的Blue Elephant，菜品做的可是泰国皇室料理。除了美食，还为大家准备了一场视听盛宴，请大家带着愉悦的心情尽情的享受。",false));
            this.list.add(new ImageRes("7",800,R.drawable.img_game_map7_false,R.drawable.img_game_map_true,"Prime奥特莱斯","Prime Outlets","泰国最大的Outlet，超过300个本地和国际品牌进驻。每日都有指定的货品两折出售，不过数量不多，看看各位能否成功淘宝。",false));
            this.list.add(new ImageRes("8",880,R.drawable.img_game_map8_false,R.drawable.img_game_map_true,"攀牙湾","Phang-Nga","泰国的小桂林，遍布着诸多大小岛屿，怪石嶙峋，景色千变万化。电影007系列还将此地作为取景地。",false));
            this.list.add(new ImageRes("9",1000,R.drawable.img_game_map9_false,R.drawable.img_game_map_true,"沙发里乐园","Safari Canteen","乐园展示了泰国当地农民的生活，在这里我们能和大象、猴子亲密接触。",false));
            UtilObjectIO.writeObject(this.list,MyApplication.mDownLoadPath);
        }
        ArrayList<Addon> tiezhiList = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath, "tiezhi");
        if (tiezhiList==null){//不存在就初始化一发 贴纸初始化
            tiezhiList = new ArrayList<>();
            tiezhiList.add(new Addon(R.drawable.img_tiezhi1,0,true));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi2,0,true));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi3,0,true));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi4,0,true));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi5,0,true));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi6,0,true));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi7,0,true));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi16_true,R.drawable.img_tiezhi16_false,false));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi10_true,R.drawable.img_tiezhi10_false,false));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi14_true,R.drawable.img_tiezhi14_false,false));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi12_true,R.drawable.img_tiezhi11_false,false));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi13_true,R.drawable.img_tiezhi13_false,false));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi9_true,R.drawable.img_tiezhi9_false,false));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi8_true,R.drawable.img_tiezhi8_false,false));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi11_true,R.drawable.img_tiezhi12_false,false));
            tiezhiList.add(new Addon(R.drawable.img_tiezhi15_true,R.drawable.img_tiezhi15_false,false));
            UtilObjectIO.writeObject(tiezhiList,MyApplication.mDownLoadPath,"tiezhi");
        }
        ArrayList<Addon> xkList = UtilObjectIO.readObject(ArrayList.class,MyApplication.mDownLoadPath,"xiangkuang");
        if (xkList == null){//相框初始化
            xkList = new ArrayList<>();
            xkList.add(new Addon(0));
            xkList.add(new Addon(R.drawable.img_xk_logo1_true,R.drawable.img_xk_logo1_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo1_true,R.drawable.img_xk_nologo1_false,false));
            xkList.add(new Addon(R.drawable.img_xk_logo2_true,R.drawable.img_xk_logo2_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo2_true,R.drawable.img_xk_nologo2_false,false));
            xkList.add(new Addon(R.drawable.img_xk_logo3_true,R.drawable.img_xk_logo3_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo3_true,R.drawable.img_xk_nologo3_false,false));
            xkList.add(new Addon(R.drawable.img_xk_logo4_true,R.drawable.img_xk_logo4_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo4_true,R.drawable.img_xk_nologo4_false,false));
            xkList.add(new Addon(R.drawable.img_xk_logo5_true,R.drawable.img_xk_logo5_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo5_true,R.drawable.img_xk_nologo5_false,false));
            xkList.add(new Addon(R.drawable.img_xk_logo6_true,R.drawable.img_xk_logo6_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo6_true,R.drawable.img_xk_nologo6_false,false));
            xkList.add(new Addon(R.drawable.img_xk_logo7_true,R.drawable.img_xk_logo7_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo7_true,R.drawable.img_xk_nologo7_false,false));
            xkList.add(new Addon(R.drawable.img_xk_logo8_true,R.drawable.img_xk_logo8_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo8_true,R.drawable.img_xk_nologo8_false,false));
            xkList.add(new Addon(R.drawable.img_xk_logo9_true,R.drawable.img_xk_logo9_false,false));
            xkList.add(new Addon(R.drawable.img_xk_nologo9_true,R.drawable.img_xk_nologo9_false,false));
            UtilObjectIO.writeObject(xkList,MyApplication.mDownLoadPath,"xiangkuang");
        }
    }

    @Override
    protected void onCreateView() {
        button = (Button) mView.findViewById(R.id.button);
        button.setOnClickListener(this);
        frameLayout = (AutoFrameLayout) mView.findViewById(R.id.frameLayout);
        iv_logo = (ImageView) mView.findViewById(R.id.iv_logo);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
            }
        });
        iv_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //上传照片
                uploadPic();
            }
        });
        tv_score = (TextView) mView.findViewById(R.id.tv_score);
        tv_unlock6 = (TextView) mView.findViewById(R.id.tv_unlock6);
        gridView = (GridView) mView.findViewById(R.id.gridView);
        adapter = new GameAdapter(getActivity(),list);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击的时候 要判断当前时间是否在2017-05-02之后 如果不是那就弹出提示框 不给扫描二维码
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd",new DateFormatSymbols(Locale.CHINA));
//                MLog.e(formatter.format(new Date()));
//                DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.CHINA);
//                MLog.e(df.format(new Date()));
//                MLog.e(UtilDate.getSecDate("2017-05-02")+"");
                String format = formatter.format(new Date());
                if (UtilDate.getSecDate("2017-05-02")>UtilDate.getSecDate(format)){//5月2号之前 弹出提示
                    showDialog();
                    return;
                }
                Intent intent = new Intent(getActivity(),Act_ZXing.class);
                startActivityForResult(intent,0);
            }
        });
        countNum();
        refreshScore();
    }

    private static final int ACTION_PHOTO = 2;
    private static final int ACTION_CUT = 3;
    private void uploadPic(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ACTION_PHOTO);
    }

    private void startPhotoZoom(Uri uri, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, ACTION_CUT);
    }

    private Bitmap photo;
    //将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata) {
        Bundle bundle = picdata.getExtras();
        if (bundle != null) {
            photo = bundle.getParcelable("data");
            iv_logo.setImageBitmap(photo);
            try {
                String path = saveFile(photo);
                MyApplication.sPreferences.edit().putString("path",path).commit();
            } catch (IOException e) {
                showToast("上传失败，请重试！");
            }
        }
    }

    /**
     * 保存文件
     * @param bm
     * @throws IOException
     */
    public static String saveFile(Bitmap bm) throws IOException {
        String path = MyApplication.mCachePath +System.currentTimeMillis()+".jpg";
        File file = new File(path);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
        return path;
    }

    private AlertDialog showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = builder
                .setTitle("温馨提示：")
                .setMessage("情迷普吉 发现之旅主题活动\n将于2017年5月2日正式开启\n更多精彩，敬请期待！")
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        alertDialog.show();
        MyApplication.post(new Runnable() {
            @Override
            public void run() {
                alertDialog.dismiss();
            }
        },5000);
        return alertDialog;
    }

    private void refreshScore(){
        //计算一下当前积分
        int score = 0;
        int n = 0;
        for (ImageRes imageRes:list) {
            if (imageRes.isLight){
                score = score+imageRes.score;
                n = n+1;
            }
        }
        if (n>=6){
            tv_unlock6.setVisibility(View.VISIBLE);
        }
        if (score == 0){
            tv_score.setVisibility(View.INVISIBLE);
        }else{
            tv_score.setVisibility(View.VISIBLE);
            tv_score.setText("当前获得积分："+score);
        }
    }

    private ResultPopwindow resultPopwindow;
    private Result2Popwindow result2Popwindow;
    private Uri imageUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0&&resultCode == 1){
            List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath);
            this.list.clear();
            this.list.addAll(list);
            adapter.notifyDataSetChanged();
            int n = 0;
            for (ImageRes imageRes:list) {
                if (imageRes.isLight){
                    n = n+1;
                }
            }
            if (n==6){
                if (result2Popwindow == null){
                    result2Popwindow = new Result2Popwindow(getActivity());
                }
                result2Popwindow.showPopupWindow(gridView);
            }else{
                ImageRes imageRes = (ImageRes) data.getSerializableExtra("ImageRes");
                if (resultPopwindow == null){
                    resultPopwindow = new ResultPopwindow(getActivity());
                }
                if (imageRes!=null){
                    resultPopwindow.setScore("并获得"+imageRes.score+"积分");
                }
                if (n == 9){
                    resultPopwindow.setTitle();
                }
                resultPopwindow.startAnim();
                resultPopwindow.showPopupWindow(gridView);
            }
            refreshScore();
        }
        switch (requestCode) {
            case ACTION_PHOTO:
                if (data != null) {
                    imageUri = data.getData();
                }
                if (imageUri != null) {
                    startPhotoZoom(imageUri, 150);
                }
                break;
            case ACTION_CUT:
                if (data != null) {
                    setPicToView(data);
                }
                break;
        }
    }

    public void countNum(){
        List<ImageRes> list = UtilObjectIO.readObject(ArrayList.class, MyApplication.mDownLoadPath);
        if(list == null){
            return;
        }
        int n = 0;
        for (ImageRes imageRes:list) {
            if (imageRes.isLight){
                n = n+1;
            }
        }
        if (n == 9){//显示主KV
            frameLayout.setVisibility(View.VISIBLE);
            //并且显示之前上传的照片
            String path = MyApplication.sPreferences.getString("path", null);
            if (path!=null){
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null){
                    iv_logo.setImageBitmap(bitmap);
                }else{
                    iv_logo.setImageResource(R.drawable.img_game_upload);
                }
            }
        }else{
            frameLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean onBackPressed() {
        return false;
    }

    @Override
    public void initUI() {

    }

    /**
     * <br>功能简述:4.4及以上获取图片的方法
     * <br>功能详细描述:
     * <br>注意:
     * @param context
     * @param uri
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {


        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;


        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];


                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {


                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));


                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];


                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }


                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };


                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {


            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();


            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }


        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {


        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };


        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
