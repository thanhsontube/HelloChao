package son.nt.hellochao.otto;

import son.nt.hellochao.base.AObject;
import son.nt.hellochao.dto.MusicItem;

/**
 * Created by Sonnt on 12/10/15.
 */
public class GoFullItemClick extends AObject {
    public MusicItem musicItem;
    public boolean isMore = false;

    public GoFullItemClick(MusicItem musicItem, boolean isMore) {
        this.musicItem = musicItem;
        this.isMore = isMore;
    }

}
