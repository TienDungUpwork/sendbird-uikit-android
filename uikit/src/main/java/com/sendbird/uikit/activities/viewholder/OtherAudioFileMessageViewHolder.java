package com.sendbird.uikit.activities.viewholder;

import android.net.Uri;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.android.Reaction;
import com.sendbird.uikit.BR;
import com.sendbird.uikit.consts.ClickableViewIdentifier;
import com.sendbird.uikit.consts.MessageGroupType;
import com.sendbird.uikit.interfaces.OnItemClickListener;
import com.sendbird.uikit.interfaces.OnItemLongClickListener;
import com.sendbird.uikit.widgets.EmojiReactionListView;
import com.sendbird.uikit.widgets.OtherAudioFileMessageView;

import java.util.List;
import java.util.Map;

public final class OtherAudioFileMessageViewHolder extends GroupChannelMessageViewHolder {
    private final EmojiReactionListView emojiReactionListView;

    OtherAudioFileMessageViewHolder(@NonNull ViewDataBinding binding, boolean useMessageGroupUI) {
        super(binding, useMessageGroupUI);
        emojiReactionListView = ((OtherAudioFileMessageView) binding.getRoot()).getBinding().rvEmojiReactionList;
        final OtherAudioFileMessageView root = (OtherAudioFileMessageView) binding.getRoot();

        clickableViewMap.put(ClickableViewIdentifier.Chat.name(), root.getBinding().ivThumbnailOveray);
        clickableViewMap.put(ClickableViewIdentifier.QuoteReply.name(), root.getBinding().quoteReplyPanel);
    }

    @Override
    public void bind(BaseChannel channel, @NonNull BaseMessage message, MessageGroupType messageGroupType) {
        binding.setVariable(BR.channel, channel);
        binding.setVariable(BR.message, message);
        binding.setVariable(BR.messageGroupType, messageGroupType);

        final OtherAudioFileMessageView root = (OtherAudioFileMessageView) binding.getRoot();
        FileMessage fileMessage = (FileMessage) message;
        String url = fileMessage.getUrl();
        root.bind(Uri.parse(url));
    }

    @Override
    public void setEmojiReaction(List<Reaction> reactionList,
                                 OnItemClickListener<String> emojiReactionClickListener,
                                 OnItemLongClickListener<String> emojiReactionLongClickListener,
                                 View.OnClickListener moreButtonClickListener) {
        emojiReactionListView.setReactionList(reactionList);
        emojiReactionListView.setEmojiReactionClickListener(emojiReactionClickListener);
        emojiReactionListView.setEmojiReactionLongClickListener(emojiReactionLongClickListener);
        emojiReactionListView.setMoreButtonClickListener(moreButtonClickListener);
    }

    @Override
    public Map<String, View> getClickableViewMap() {
        return clickableViewMap;
    }
}
