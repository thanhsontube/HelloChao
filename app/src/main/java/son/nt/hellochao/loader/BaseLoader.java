package son.nt.hellochao.loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

import java.io.File;
import java.net.URL;

/**
 * Created by Sonnt on 10/12/15.
 */
public abstract class BaseLoader<T> {
    private static final String TAG = "BaseLoader";
    public abstract void onLoaderStart ();
    public abstract void onLoaderSuccess(T entity);
    public abstract void onLoaderFail(Throwable e);

    Context context;
    String link;
    boolean isSaveToXml = false;
    String pathFolder = null;

    public BaseLoader (Context context) {
        this (context, null, false);

    }

    public BaseLoader (Context context, String link) {
        this (context, link, false);
    }


    private BaseLoader (Context context, String link, boolean isSaveToXml) {
        this.context = context;
        this.link = link;
        this.isSaveToXml = isSaveToXml;

    }

    public void setPathFolder(String pathFolder) {
        this.isSaveToXml = true;
        this.pathFolder = pathFolder;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setIsSaveToXml(boolean isSaveToXml) {
        this.isSaveToXml = isSaveToXml;
    }

    protected abstract T handleTagNode (TagNode tagNode, String link);

    public  void execute (LoaderManager manager) {
        task.execute(manager);

    }
    public  void cancel() {
        task.cancel(true);
    }

    private final AsyncTask <LoaderManager, Void, T> task = new AsyncTask<LoaderManager, Void, T>() {
        private Throwable error = null;

        @Override
        protected void onPreExecute() {
            if (isCancelled()) {
                return;
            }
            super.onPreExecute();
            onLoaderStart();
        }

        @Override
        protected T doInBackground(LoaderManager... loaderManagers) {
            if (isCancelled()) {
                return null;
            }

            final LoaderManager manager = loaderManagers[0];
            T entity = null;

            try {

                if (!isNetworkConnected()) {
                    error = new Throwable("No Network Connection !");
                    return null;
                }
                CleanerProperties props = new CleanerProperties();

// set some properties to non-default values
                props.setTranslateSpecialEntities(true);
                props.setTransResCharsToNCR(true);
                props.setOmitComments(true);

// do parsing
                TagNode tagNode = new HtmlCleaner(props).clean(
                        new URL(link)
                );

// serialize to xml file
                if (isSaveToXml && pathFolder != null) {
                    String temp = link.replaceAll("[\\-\\+\\.\\^:,]","").replaceAll("/","");
                    String pathSave = pathFolder + File.separator + temp + ".xml";

//                    Log.v(TAG, ">>>" + "pathSave:" + pathSave);
                    new PrettyXmlSerializer(props).writeToFile(
                            tagNode, pathSave, "utf-8"
                    );
                }

                if (tagNode != null) {
                    entity = handleTagNode(tagNode, link);
                }
                if (entity == null) {
                    error = new Throwable("tagNode from :" + link + " is NULL");
                }
                return entity;

            } catch (Exception e) {
                Log.e(TAG, ">>>" + "Error: " + e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(T result) {
            super.onPostExecute(result);
            if (isCancelled()) {
                return;
            }

            if (error != null) {
                onLoaderFail(error);
            } else if (result == null) {
                onLoaderFail(new NullPointerException("tagNode from :" + link + " is NULL"));
            } else {
                onLoaderSuccess(result);
            }

        }
    };

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }

}
