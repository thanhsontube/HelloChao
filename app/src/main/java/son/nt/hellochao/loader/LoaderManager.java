package son.nt.hellochao.loader;

/**
 * Created by Sonnt on 10/12/15.
 */
public class LoaderManager {
    public void execute (HtmlCleanerBaseLoader<?> loader) {
        loader.execute(this);
    }

    public void cancel (HtmlCleanerBaseLoader<?> loader) {
        loader.cancel();
    }
}
