package son.nt.hellochao.otto;

import son.nt.hellochao.base.AObject;
import son.nt.hellochao.dto.MusicItem;

/**
 * Created by Sonnt on 12/10/15.
 */
public class GoFullItemClick extends AObject {
    public MusicItem musicItem;

    public GoFullItemClick(MusicItem musicItem) {
        this.musicItem = musicItem;
    }
}
