package com.platformstory.modelhouse.Common;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by midasyoon on 2017. 3. 28..
 */

public class GooglelMapUtils {
    // 클러스터를 띄우는데 사용되는 위도,경도 값을 저장하기 위한 객체 정의
    public static class MyItem implements ClusterItem {
        private final LatLng mPosition;

        public MyItem(double lat, double lng) {
            mPosition = new LatLng(lat, lng);
        }

        @Override
        public LatLng getPosition() {
            return mPosition;
        }
    }

    public static class CustomRenderer<T extends ClusterItem> extends DefaultClusterRenderer<T> {
        public CustomRenderer(Context context, GoogleMap map, ClusterManager<T> clusterManager) {
            super(context, map, clusterManager);
        }

        // 클러스터를 형성할 최소의 마커 개수를 1로 설정
        @Override
        protected boolean shouldRenderAsCluster(Cluster<T> cluster) {
            //start clustering if at least 1 items overlap
            return cluster.getSize() >= 1;
        }

        // 클러스터의 마커 개수를 대략적인 값이 아닌 정확한 수치로 표시
        @Override
        protected int getBucket(Cluster<T> cluster) {
            int size = cluster.getSize();

            return size;
        }

        @Override
        protected String getClusterText(int bucket) {
            return bucket+"";
        }
    }
}
