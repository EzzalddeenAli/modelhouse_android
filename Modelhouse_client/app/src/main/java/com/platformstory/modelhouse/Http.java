package com.platformstory.modelhouse;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016-01-25.
 */
/*
	 * Http request Get, Post
	 *
	 * [sample code] : HttpTestActivity (located in package
	 * me.halalfind.app.halalkorea.common)
	 */
public class Http {
    private static class BitmapNamePair {
        private Bitmap bitmap;
        private String imageName;

        public BitmapNamePair(Bitmap bitmap, String imageName) {
            // TODO Auto-generated constructor stub
            this.bitmap = bitmap;
            this.imageName = imageName;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }
        public String getImageName() {
            return imageName;
        }
    }

    private static class VideoNamePair {
        private File file;
        private String videoName;
        private String videoPath;
//			private Bitmap videoThumnail;

        public VideoNamePair(File file, String videoName)
        {
//				this.videoThumnail = videoThumnail;
//            this.videoPath = videoPath;
            this.videoName = videoName;
            this.file = file;
        }

        //			public Bitmap getVideoThumnail() { return videoThumnail; }
        public String getVideoName() { return videoName; }
        public String getVideoPath() { return videoPath; }
        public File getFile() { return file; }

    }

    public static class ParamModel {
        private String url;
        private String paramStr;
        private List<BitmapNamePair> bitmaps;
        private List<VideoNamePair> videos = null;

        private String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        private String getParamStr() {
            return paramStr;
        }
        public void setParamStr(String paramStr) {
            this.paramStr = paramStr;
        }
        private List<BitmapNamePair> getBitmaps() {
            return bitmaps;
        }
        public void addBitmap(Bitmap bitmap, String imageName) {
            if (bitmaps == null) {
                bitmaps = new ArrayList<BitmapNamePair>();
            }
            bitmaps.add(new BitmapNamePair(bitmap, imageName));
        }
        private List<VideoNamePair> getVideos() { return videos; }
        public void addVideo(File file, String videoName)
        {
            if(videos == null)
            {
                videos = new ArrayList<VideoNamePair>();
            }
            videos.add(new VideoNamePair(file, videoName));
        }
    }

    public static final int METHOD_GET = 2;
    public static final int METHOD_POST = 3;

    private static final int CONNECTION_TIME_OUT_MILLIS = 60000;
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final int DEFAULT_METHOD = METHOD_POST;

    private static final String boundary = "*****";
    private static final String twoHyphens = "--";
    private static final String crlf = "\r\n";
    private static final String attachmentFileName = "image.jpg";

    private static final String HTTP_TAG = "[HTTP]";

    private String encoding;
    private int method;

    private ProgressDialog getmProgressDialog = null;

    public Http() {
        encoding = DEFAULT_ENCODING;
        method = DEFAULT_METHOD;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setMethod(int method) {
        this.method = method;
    }

    /* overload stuff */
    public String send(ParamModel paramModel) {
        return send(paramModel, method);
    }

    public String send(ParamModel paramModel, int method) {
        if ((method != METHOD_GET) && (method != METHOD_POST)) {
            Log.d(HTTP_TAG, "Error: Wrong request method.");
            return null;
        }

        String result = null;
        try {
            final URL url = new URL(paramModel.getUrl());

            String paramStr = paramModel.getParamStr();
            if (paramStr == null) {
                paramStr = "";
            }

            Log.d(HTTP_TAG, "url: " + url.toString() + ", params: " + paramStr);

            HttpURLConnection urlConnection = null;
            OutputStream out = null;
            InputStream in = null;
            BufferedReader bReader = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(CONNECTION_TIME_OUT_MILLIS);
                urlConnection.setRequestMethod(getMethodStr(method));
                urlConnection.setChunkedStreamingMode(0);

                final List<BitmapNamePair> bitmapNamePairs = paramModel.getBitmaps();
                if ((bitmapNamePairs != null) && (!bitmapNamePairs.isEmpty())) {
                    Log.d(HTTP_TAG, "bitmap is not null.");

                    urlConnection.setUseCaches(false);
                    urlConnection
                            .setRequestProperty("Connection", "Keep-Alive");
                    urlConnection.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
                }

                out = new BufferedOutputStream(urlConnection.getOutputStream());

                if (bitmapNamePairs == null) {
                    out.write(paramStr.getBytes(encoding));
                }
                else {
                    String[] pairs = paramStr.split("&");
                    for (String pair : pairs) {
                        int idx = pair.indexOf("=");
                        writeFormField(out, URLDecoder.decode(
                                pair.substring(0, idx), encoding),
                                URLDecoder.decode(pair.substring(idx + 1),
                                        encoding));
                    }
                    out.write(crlf.getBytes(encoding));
                    out.write((twoHyphens + boundary + crlf).getBytes(encoding));

						/*
						out.write(("Content-Disposition: form-data; name=\""
								+ attachmentName + "\";filename=\""
								+ attachmentFileName + "\"" + crlf)
								.getBytes(encoding));
						out.write(("Content-Type: image/jpg" + crlf)
								.getBytes(encoding));
						out.write(crlf.getBytes(encoding));
						*/

                    for (int i = 0; i < bitmapNamePairs.size(); i++) {
                        //for (Bitmap bitmap : bitmaps) {
                        BitmapNamePair bitmapNamePair = bitmapNamePairs.get(i);

                        out.write(("Content-Disposition: form-data; name=\""
                                + bitmapNamePair.getImageName() + "\";filename=\""
                                + attachmentFileName + "\"" + crlf)
                                .getBytes(encoding));
                        out.write(("Content-Type: image/jpg" + crlf)
                                .getBytes(encoding));
                        out.write(crlf.getBytes(encoding));

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        if (bitmapNamePair.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bos)) {
                            out.write(bos.toByteArray());
                        }
                        else {
                            Log.d(HTTP_TAG, "Bitmap compress failed.");
                        }

                        out.write(crlf.getBytes(encoding));
                        out.write((twoHyphens + boundary + crlf).getBytes(encoding));
                    }

                    //out.write((twoHyphens + boundary + crlf).getBytes(encoding));
                }

                Log.d(HTTP_TAG, "Sending...");
                out.flush();
                Log.d(HTTP_TAG, "Sending done.");

                Log.d(HTTP_TAG, "response code: " + urlConnection.getResponseCode()
                        + ", message: " + urlConnection.getResponseMessage());
                in = new BufferedInputStream(urlConnection.getInputStream());

                bReader = new BufferedReader(new InputStreamReader(in, encoding));
                StringBuilder strBuilder = new StringBuilder();

                String line;
                while ((line = bReader.readLine()) != null) {
                    strBuilder.append(line);
                }

                result = strBuilder.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    bReader.close();
                    in.close();
                    out.close();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NullPointerException e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d(HTTP_TAG, "result: " + result);
        return result;
    }

    public String videosend(ParamModel paramModel, ProgressDialog getmProgressDialog) {

        return videosend(paramModel, method, getmProgressDialog );
    }

    public String videosend(ParamModel paramModel, int method, ProgressDialog getmProgressDialog) {
        if ((method != METHOD_GET) && (method != METHOD_POST)) {
            Log.d(HTTP_TAG, "Error: Wrong request method.");
            return null;
        }
        Log.d("jjo-debug", "videosend http");
        String result = null;
        try {
            final URL url = new URL(paramModel.getUrl());
            this.getmProgressDialog = getmProgressDialog;
            String paramStr = paramModel.getParamStr();
            if (paramStr == null) {
                paramStr = "";
            }

            Log.d(HTTP_TAG, "url: " + url.toString() + ", params: " + paramStr);

            String videoName, videoPath;

            HttpURLConnection urlConnection = null;
            DataOutputStream out = null;
            InputStream in = null;
            BufferedReader bReader = null;

            int byteRead, bytesAvailable, buffersize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            double transportbytes = 0;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setConnectTimeout(CONNECTION_TIME_OUT_MILLIS);
                urlConnection.setRequestMethod(getMethodStr(method));
                urlConnection.setChunkedStreamingMode(0);

                final List<BitmapNamePair> bitmapNamePairs = paramModel.getBitmaps();
                final List<VideoNamePair> videoNamePairs = paramModel.getVideos();
                if ((bitmapNamePairs != null) && (!bitmapNamePairs.isEmpty())) {
                    Log.d(HTTP_TAG, "bitmap is not null.");

                    urlConnection.setUseCaches(false);
                    urlConnection
                            .setRequestProperty("Connection", "Keep-Alive");  // requset Header 媛??뗮똿
                    urlConnection.setRequestProperty("Content-Type",
                            "multipart/form-data;boundary=" + boundary);
//						urlConnection.setRequestProperty("Accept-Encoding", "identity");

                }

                out = new DataOutputStream(urlConnection.getOutputStream());

                if (bitmapNamePairs == null) {
                    out.writeBytes(paramStr);
                } else {
                    String[] pairs = paramStr.split("&");

                    for (String pair : pairs) {
                        int idx = pair.indexOf("=");
                        writeFormField(out, URLDecoder.decode(
                                pair.substring(0, idx), encoding),
                                URLDecoder.decode(pair.substring(idx + 1),
                                        encoding));
                    }
                    out.writeBytes(crlf);
                    out.writeBytes((twoHyphens + boundary + crlf));

						/*
						out.write(("Content-Disposition: form-data; name=\""
								+ attachmentName + "\";filename=\""
								+ attachmentFileName + "\"" + crlf)
								.getBytes(encoding));
						out.write(("Content-Type: image/jpg" + crlf)
								.getBytes(encoding));
						out.write(crlf.getBytes(encoding));
						*/


                    for (int i = 0; i < bitmapNamePairs.size(); i++) {
                        //for (Bitmap bitmap : bitmaps) {
                        BitmapNamePair bitmapNamePair = bitmapNamePairs.get(i);


                        out.writeBytes(("Content-Disposition: form-data; name=\"" + bitmapNamePair.getImageName() + "\";filename=\"" + attachmentFileName + "\"" + crlf));
                        out.writeBytes(("Content-Type: image/jpg" + crlf));
                        out.writeBytes(crlf);


                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        if (bitmapNamePair.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bos)) {
                            out.write(bos.toByteArray());
                        } else {
                            Log.d(HTTP_TAG, "Bitmap compress failed.");
                        }
                        //	bos.close();
                    }
                    if (videoNamePairs != null) {
                        videoName = videoNamePairs.get(0).getVideoName();
                        videoPath = videoNamePairs.get(0).getVideoPath();
                        File videofile = new File(videoPath);
                        // ?뚯씪??議댁옱????
                        if (videofile.exists()) {
                            Log.d(HTTP_TAG, "video exist...");
                            Log.d(HTTP_TAG, "videoPath = " + videoPath);
                            long videofilesize = videofile.length();
                            // ?뚯씪 ?쎄린
                            FileInputStream filein = new FileInputStream(videofile);

                            Log.d(HTTP_TAG, "video size = " + videofilesize);


                            out.writeBytes(crlf);
                            out.writeBytes((twoHyphens + boundary + crlf));
                            out.writeBytes(("Content-Disposition: form-data; name=\"" + videoName + "\";filename=\"" + videoPath + "\"" + crlf));
                            out.writeBytes(("Content-Type: video/mp4" + crlf));
                            out.writeBytes(crlf);


                            bytesAvailable = filein.available();  // ?꾩옱 ?쎌쓣 ???덈뒗 諛붿씠????諛섑솚
                            Log.d(HTTP_TAG, "Inital .available" + bytesAvailable + " videofile size = " + videofilesize);

                            buffersize = Math.min(bytesAvailable, maxBufferSize); // 理쒖냼媛믪쓣 踰꾪띁?ъ씠利덉뿉 ???
                            buffer = new byte[buffersize];

                            byteRead = filein.read(buffer, 0, buffersize);
                            int progress;
                            DataOutputStream output2 = new DataOutputStream(urlConnection.getOutputStream());
                            while (byteRead > 0) {

                                output2.write(buffer, 0, buffersize);
                                bytesAvailable = filein.available();
                                buffersize = Math.min(bytesAvailable, maxBufferSize);
                                transportbytes += byteRead;
                                progress = (int) ((transportbytes / videofilesize) * 100);
                                getmProgressDialog.setProgress(progress);
                                byteRead = filein.read(buffer, 0, buffersize);
                            }


                            getmProgressDialog.setProgress(100);


                            //								filein.close();
                        }
                    }
                    out.writeBytes(crlf);
                    out.writeBytes((crlf + twoHyphens + boundary + crlf));

                    //out.write((twoHyphens + boundary + crlf).getBytes(encoding));
                }

                Log.d(HTTP_TAG, "Sending...");
                out.flush();  // ?쒕쾭濡??꾩넚
                Log.d(HTTP_TAG, "Sending done.");

                Log.d(HTTP_TAG, "response code: " + urlConnection.getResponseCode()
                        + ", message: " + urlConnection.getResponseMessage());
                in = new BufferedInputStream(urlConnection.getInputStream());
                bReader = new BufferedReader(new InputStreamReader(in, encoding));
                StringBuilder strBuilder = new StringBuilder();
                String line;
                while ((line = bReader.readLine()) != null) {
                    strBuilder.append(line);
                }
                result = strBuilder.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    bReader.close();
                    in.close();
                    out.close();
                    urlConnection.disconnect();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NullPointerException e2) {
                    // TODO: handle exception
                    e2.printStackTrace();
                }
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Log.d(HTTP_TAG, "result: " + result);
        return result;
    }

    /**
     * write one form field to dataSream
     *
     * @param out
     * @param fieldName
     * @param fieldValue
     * @throws IOException
     * @throws UnsupportedEncodingException
     */
    private void writeFormField(OutputStream out, String fieldName,
                                String fieldValue) throws UnsupportedEncodingException,
            IOException {
        out.write((twoHyphens + boundary + crlf).getBytes(encoding));
        out.write(("Content-Disposition: form-data; name=\"" + fieldName
                + "\"" + crlf).getBytes(encoding));
        out.write(crlf.getBytes(encoding));
        out.write(fieldValue.getBytes(encoding));
        out.write(crlf.getBytes(encoding));
    }

    private String getMethodStr(int method) {
        String methodStr;
        if (method == METHOD_GET) {
            methodStr = "GET";
        } else {
            methodStr = "POST";
        }
        return methodStr;
    }
}