package son.nt.hellochao.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Auto-Scrolling marquee text view
 * 
 * Source: http://androidbears.stellarpc.net/?p=185
 * 
 * @author http://androidbears.stellarpc.net/?page_id=195
 *
 */
public class ScrollingTextView extends TextView
{
    public ScrollingTextView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
    }

    public ScrollingTextView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    public ScrollingTextView(final Context context)
    {
        super(context);
    }

    @Override
    protected void onFocusChanged(final boolean focused, final int direction, final Rect previouslyFocusedRect)
    {
        if (focused)
        {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    @Override
    public void onWindowFocusChanged(final boolean focused)
    {
        if (focused)
        {
            super.onWindowFocusChanged(focused);
        }
    }

    /**
     * Force this UI element to always think it is focused and therefore the marquee should play.
     * 
     * @see android.view.View#isFocused()
     */
    @Override
    public boolean isFocused()
    {
        return true;
    }

}
