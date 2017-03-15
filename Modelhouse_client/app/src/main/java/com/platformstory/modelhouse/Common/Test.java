//package com.platformstory.modelhouse;
//
//import android.os.AsyncTask;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Toast;
//
//import com.google.ads.AdRequest;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by Midas Yoon on 2017-03-06.
// */
//public class Test {
//    private void getUserCastingListData() {
//        String url = CommonValues.URL_SERVER + CONNECT_URL;
//        String paramStr = "session_id=" + SharedPreference.getUserSessionID(getActivity());
//
//
//        Http.ParamModel params = new Http.ParamModel();
//        params.setUrl(url);
//        params.setParamStr(paramStr);
//
//        //AsyncTask
//        getUserCastingListDataTask = new GetUserCastingListDataTask();
//        getUserCastingListDataTask.execute(params);
//    }
//
//    private class GetUserCastingListDataTask extends AsyncTask<Http.ParamModel, Void, Integer> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Integer doInBackground(Http.ParamModel... params) {
//
//            Http http = new Http();
//            String result = http.send(params[0]);
//
//            if (TextUtils.isEmpty(result)) {
//                return AdRequest.ErrorCode.INIT;
//            }
//
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//
//                int code = jsonObject.getInt("code");
//
//                switch (code) {
//
//                    case AdRequest.ErrorCode.SUCCESS:
//                        JSONArray jsonArray = jsonObject.getJSONArray("data");
//
//                        if (jsonArray.length() == 0) {
//                            return AdRequest.ErrorCode.ERR_DB_NODATA;
//                        }
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObj = jsonArray.getJSONObject(i);
//
//                            CastingModel model = new CastingModel();
//                            model.setId(jsonObj.getInt("id"));
//                            model.setEvent_name(jsonObj.getString("event_name"));
//                            model.setCasting_start_date(jsonObj.getString("start_date"));
//                            model.setCasting_end_date(jsonObj.getString("end_date"));
//                            model.setEvent_date(jsonObj.getString("event_date"));
//                            model.setDayover(jsonObj.getString("dayover"));
//                            model.setD_day(jsonObj.getString("days"));
//                            model.setRemain_time(jsonObj.getString("days") + "일 " + jsonObj.getString("hours") + "시간 " + jsonObj.getString("minutes") + "분");
//                            model.setFlag(jsonObj.getString("flag"));
//                            model.setEvent_address(jsonObj.getString("address"));
//                            model.setEvent_start_time(jsonObj.getString("start_time"));
//                            model.setEvent_end_time(jsonObj.getString("end_time"));
//                            model.setEvent_target_people(jsonObj.getString("target"));
//                            model.setEvent_people_num(jsonObj.getString("target_num"));
//                            model.setBid_id(jsonObj.getString("engchal_id"));
//                            model.setBid_flag(jsonObj.getString("engchal"));
//                            model.setDuration(jsonObj.getInt("duration"));
//
//                            if (jsonObj.getString("flag").equals("M")) {
//                                model.setCasting_large(jsonObj.getString("job"));
//                                model.setCasting_small(jsonObj.getString("category"));
//                                model.setCasting_count(jsonObj.getInt("count"));
//                                model.setBid_finish(jsonObj.getString("finish"));
//                            } else if (jsonObj.getString("flag").equals("S")) {
//                                model.setEntertainer_name(jsonObj.getString("name"));
//                            }
//
//                            models.add(model);
//                        }
//                        break;
//
//                    case AdRequest.ErrorCode.ERR_DB_NODATA:
//                        break;
//                }
//
//                return code;
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            return AdRequest.ErrorCode.INIT;
//        }
//
//        @Override
//        protected void onPostExecute(Integer code) {
//
//            switch (code) {
//                case AdRequest.ErrorCode.SUCCESS:
//                    adapter.notifyDataSetChanged();
//                    txt_no_data.setVisibility(View.GONE);
//                    break;
//                case AdRequest.ErrorCode.ERR_DB_NODATA:
//                    adapter.notifyDataSetChanged();
//                    txt_no_data.setVisibility(View.VISIBLE);
//                    break;
//                case AdRequest.ErrorCode.INIT:
//                    Toast.makeText(getActivity(), getString(R.string.init), Toast.LENGTH_SHORT).show();
//                    break;
//            }
//        }
//    }
//
//}
