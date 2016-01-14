package son.nt.hellochao.interface_app;

import son.nt.hellochao.parse_object.PointDto;

/**
 * Created by Sonnt on 11/14/15.
 */
public interface IPoint {

    void setLinkWithFacebook();
    void setPointCallback (IPoint.Callback callback);
    interface Callback {

        void onAddedPoint(PointDto pointDto);

    }

}
