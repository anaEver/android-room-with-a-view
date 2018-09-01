package ir.peivandyar.database.repository;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import ir.peivandyar.database.DataBase;
import ir.peivandyar.database.dao.SchoolInfoDao;
import ir.peivandyar.database.entity.SchoolInfo;

/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */

public class SchoolInfoRepository {

    private SchoolInfoDao mSchoolInfoDao;
    private LiveData<List<SchoolInfo>> mAllSchoolInfos;

    // Note that in order to unit test the SchoolInfoRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public SchoolInfoRepository(Application application) {
        DataBase db = DataBase.getDatabase(application);
        mSchoolInfoDao = db.schoolInfoDao();
        mAllSchoolInfos = mSchoolInfoDao.getSchools();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<SchoolInfo>> getAllSchoolInfos() {
        return mAllSchoolInfos;
    }

    public LiveData<List<String>> getProvinces() {
        return mSchoolInfoDao.getProvinces();
    }

    public LiveData<List<String>> getTowns() {
        return mSchoolInfoDao.getTowns();
    }

    public LiveData<List<Integer>> getRegions(){
        return mSchoolInfoDao.getRegions();
    }

    public void insertAll(List<SchoolInfo> schoolInfos) {
        for (int i = 0; i < schoolInfos.size(); i++) {
            insert(schoolInfos.get(i));
        }
    }

    // You must call this on a non-UI thread or your app will crash.
    // Like this, Room ensures that you're not doing any long running operations on the main
    // thread, blocking the UI.
    public void insert(SchoolInfo schoolInfo) {
        new insertAsyncTask(mSchoolInfoDao).execute(schoolInfo);
    }

    private static class insertAsyncTask extends AsyncTask<SchoolInfo, Void, Void> {

        private SchoolInfoDao mAsyncTaskDao;

        insertAsyncTask(SchoolInfoDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final SchoolInfo... params) {
            SchoolInfo tmp = mAsyncTaskDao.getSchoolInfo(params[0].getId());
            if (tmp == null)
                mAsyncTaskDao.insert(params[0]);
            else if(tmp.getLastRefreshed() < params[0].getLastRefreshed()){
                mAsyncTaskDao.deleteRow(tmp.getId());
                mAsyncTaskDao.insert(params[0]);
            }
            return null;
        }
    }
}
