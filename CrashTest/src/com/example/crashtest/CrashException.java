package com.example.crashtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import android.R.string;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.Looper;
import android.provider.ContactsContract.Directory;
import android.util.Log;
import android.widget.Toast;

public class CrashException implements UncaughtExceptionHandler {

	 public static final String TAG = "CrashHandler";    
    //ϵͳĬ�ϵ�UncaughtException������       
    private Thread.UncaughtExceptionHandler mDefaultHandler;      
    //CrashHandlerʵ��      
    private static CrashException instance;  
   //�����Context����      
    private Context mContext;     
    //�����洢�豸��Ϣ���쳣��Ϣ  
    private Map<String, String> infos = new HashMap<String, String>();  
  
    //���ڸ�ʽ������,��Ϊ��־�ļ����һ����  
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  
    
    private String filePath;
    private static final String CRASH_REPORTER_EXTENSION = ".cr";   
	
	   /** ��ȡCrashHandlerʵ�� ,����ģʽ */      
    public static CrashException getInstance() {      
        if(instance == null)  
            instance = new CrashException();     
        return instance;      
    }      
	 /**   
     * ��ʼ��   
     */      
    public void init(Context context) {      
        mContext = context;     
        filePath = GetFilePath();
        //��ȡϵͳĬ�ϵ�UncaughtException������      
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();      
        //���ø�CrashHandlerΪ�����Ĭ�ϴ�����      
        Thread.setDefaultUncaughtExceptionHandler(this);      
    }      
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
        Log.e(TAG,"SSSSSSSSSSSSSSSSSSSSSSSSSSSS");
				  if (!handleException(ex) && mDefaultHandler != null) {      
			            //����û�û�д�������ϵͳĬ�ϵ��쳣������������      
			            mDefaultHandler.uncaughtException(thread, ex);      
			        } else {      
			            try {      
			                Thread.sleep(3000);      
			            } catch (InterruptedException e) {      
			                Log.e(TAG, "error : ", e);      
			            }      
			            //�˳�����      
			            android.os.Process.killProcess(android.os.Process.myPid());      
			            System.exit(1);      
			        }      
			}
/**   
 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.   
 *    
 * @param ex   
 * @return true:������˸��쳣��Ϣ;���򷵻�false.   
 */      
private boolean handleException(Throwable ex) {      
    if (ex == null) {      
        return false;      
    }      
    collectDeviceInfo(mContext);
    saveCrashInfo2File(ex);
    //ʹ��Toast����ʾ�쳣��Ϣ      
    new Thread() {      
        @Override      
        public void run() {      
            Looper.prepare();      
            Toast.makeText(mContext, "�ܱ�Ǹ,��������쳣,�����˳�.", Toast.LENGTH_SHORT).show();      
            Looper.loop();      
        }      
    }.start();      
    //������־�ļ�         
    return true;      
}     
/** 
 * �ռ��豸������Ϣ 
 * @param ctx 
 */  
public void collectDeviceInfo(Context ctx) {  
    try {  
        PackageManager pm = ctx.getPackageManager();  
        PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);  
        if (pi != null) {  
            String versionName = pi.versionName == null ? "null" : pi.versionName;  
            String versionCode = pi.versionCode + "";  
            infos.put("versionName", versionName);  
            infos.put("versionCode", versionCode);  
        }  
    } catch (NameNotFoundException e) {  
        Log.e(TAG, "an error occured when collect package info", e);  
    }  
    Field[] fields = Build.class.getDeclaredFields();  
    for (Field field : fields) {  
        try {  
            field.setAccessible(true);  
            infos.put(field.getName(), field.get(null).toString());  
            Log.d(TAG, field.getName() + " : " + field.get(null));  
        } catch (Exception e) {  
            Log.e(TAG, "an error occured when collect crash info", e);  
        }  
    }  
}  

/** 
 * ���������Ϣ���ļ��� 
 *  
 * @param ex 
 * @return  �����ļ����,���ڽ��ļ����͵������� 
 */  
private String saveCrashInfo2File(Throwable ex) {  
      
    StringBuffer sb = new StringBuffer();  
    for (Map.Entry<String, String> entry : infos.entrySet()) {  
        String key = entry.getKey();  
        String value = entry.getValue();  
        sb.append(key + "=" + value + "\n");  
    }  
      
    Writer writer = new StringWriter();  
    PrintWriter printWriter = new PrintWriter(writer);  
    ex.printStackTrace(printWriter);  
    Throwable cause = ex.getCause();  
    while (cause != null) {  
        cause.printStackTrace(printWriter);  
        cause = cause.getCause();  
    }  
    printWriter.close();  
    String result = writer.toString();  
    sb.append(result);  
    
    SendMaile sendMaile = SendMaile.getInstance();
    sendMaile.send(sb.toString());
   // WriteFile(sb.toString());
    return null;  
} 

private String GetFilePath() {
	 if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {  
	        String path= mContext.getFilesDir().getPath() +"/CrashReport/";
	            File dir =new File(path);  
	            if (!dir.exists()) {  
	                dir.mkdirs();  
	            }  
	            return dir.getPath();
	 }
	 return "";
}

public void WriteFile(String exceptionContent)
{  
	Log.e("Path", mContext.getFilesDir().getPath());
	long timestamp = System.currentTimeMillis();  
	String time = formatter.format(new Date());  
	String fileName = "crash-" + time + "-" + timestamp +CRASH_REPORTER_EXTENSION;  
	if(!filePath.equals(""))
	{
	    FileOutputStream fos;
		try {
			fos = new FileOutputStream(filePath+"/"+fileName);
			fos.write(exceptionContent.getBytes());
	        fos.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    Log.e("Write", "Success");
	    sendPreviousReportsToServer();
	}
	else
	{
		  Log.e("Write", "Faile");
	}
}  

public void sendPreviousReportsToServer() {   
    sendCrashReportsToServer(mContext);   
}   

/**  
 * �Ѵ��󱨸淢�͸������,���²���ĺ���ǰû���͵�.  
 * @param ctx  
 */   
 private void sendCrashReportsToServer(Context ctx) {   
     String[] crFiles = getCrashReportFiles(ctx);   
     if (crFiles != null && crFiles.length > 0) {   
     TreeSet<String> sortedFiles = new TreeSet<String>();   
     sortedFiles.addAll(Arrays.asList(crFiles));   
     for (String fileName : sortedFiles) {   
     File cr = new File(filePath, fileName);   
     postReport(cr);   
     cr.delete();// ɾ���ѷ��͵ı���   
     }   
     }   
 }   

 private void postReport(File file) {   
     // TODO ���ʹ��󱨸浽������   
	 Log.e("postReport", file.getAbsolutePath());
	 FTPUtils ftpUtils = FTPUtils.getInstance();
	 ftpUtils.uploadFile(file.getAbsolutePath(), file.getName());
 }  
 
 /**  
  * ��ȡ���󱨸��ļ���  
  * @param ctx  
  * @return  
  */   
  private String[] getCrashReportFiles(Context ctx) {   
      File filesDir = new File(filePath);   
      FilenameFilter filter = new FilenameFilter() {   
          public boolean accept(File dir, String name) {   
              return name.endsWith(CRASH_REPORTER_EXTENSION);   
          }   
      };   
      return filesDir.list(filter);   
  }   
}

