package com.sendbird.uikit.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;

import com.sendbird.android.BaseMessage;
import com.sendbird.android.FileMessage;
import com.sendbird.uikit.R;
import com.sendbird.uikit.consts.StringSet;
import com.sendbird.uikit.databinding.SbViewMessagePreviewBinding;
import com.sendbird.uikit.utils.DateUtils;
import com.sendbird.uikit.utils.DrawableUtils;
import com.sendbird.uikit.utils.ViewUtils;

public class MessagePreview extends FrameLayout {
    private SbViewMessagePreviewBinding binding;
    private int metaPhorTintColor;
    private int messageTextAppearance;
    private int messageFileTextAppearance;

    public MessagePreview(Context context) {
        this(context, null);
    }

    public MessagePreview(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.sb_message_preview_style);
    }

    public MessagePreview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MessagePreview, defStyle, 0);
        try {
            this.binding = SbViewMessagePreviewBinding.inflate(LayoutInflater.from(getContext()));
            addView(binding.getRoot(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            int background = a.getResourceId(R.styleable.MessagePreview_sb_message_preview_background, R.drawable.selector_rectangle_light);
            int userNameAppearance = a.getResourceId(R.styleable.MessagePreview_sb_message_preview_username_text_appearance, R.style.SendbirdSubtitle1OnLight01);
            messageTextAppearance = a.getResourceId(R.styleable.MessagePreview_sb_message_preview_message_text_appearance, R.style.SendbirdBody3OnLight03);
            messageFileTextAppearance = a.getResourceId(R.styleable.MessagePreview_sb_message_preview_message_file_text_appearance, R.style.SendbirdBody3OnLight01);
            int sentAtTextAppearance = a.getResourceId(R.styleable.MessagePreview_sb_message_preview_sent_at_text_appearance, R.style.SendbirdCaption2OnLight02);
            int dividerColor = a.getResourceId(R.styleable.MessagePreview_sb_message_preview_divider_color, R.color.onlight_04);
            int metaphorBackgroundColor = a.getResourceId(R.styleable.MessagePreview_sb_message_preview_message_metaphor_background_color, R.color.background_100);
            this.metaPhorTintColor = a.getResourceId(R.styleable.MessagePreview_sb_message_preview_message_metaphor_icon_tint_color, R.color.primary_300);

            binding.root.setBackgroundResource(background);
            binding.tvUserName.setTextAppearance(context, userNameAppearance);
            binding.tvMessage.setTextAppearance(context, messageTextAppearance);
            binding.tvSentAt.setTextAppearance(context, sentAtTextAppearance);
            binding.ivDivider.setBackgroundResource(dividerColor);
            binding.ivIcon.setBackgroundDrawable(DrawableUtils.createRoundedRectangle(getResources().getDimension(R.dimen.sb_size_8), ContextCompat.getColor(context, metaphorBackgroundColor)));
        } finally {
            a.recycle();
        }
    }

    public void drawMessage(@NonNull BaseMessage message) {
        final Context context = this.binding.tvSentAt.getContext();
        ViewUtils.drawProfile(binding.ivProfile, message);
        ViewUtils.drawNickname(binding.tvUserName, message);
        this.binding.tvSentAt.setText(DateUtils.formatDateTime(context, message.getCreatedAt()));
        if (message instanceof FileMessage) {
            FileMessage fileMessage = (FileMessage) message;
            int icon = getIconDrawable(fileMessage.getType());

            this.binding.tvMessage.setSingleLine(true);
            this.binding.tvMessage.setMaxLines(1);
            this.binding.tvMessage.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            this.binding.tvMessage.setTextAppearance(context, messageFileTextAppearance);
            this.binding.ivIcon.setImageDrawable(DrawableUtils.setTintList(binding.ivIcon.getContext(), icon, metaPhorTintColor));
            this.binding.ivIcon.setImageResource(icon);
            this.binding.ivIcon.setVisibility(View.VISIBLE);
            this.binding.tvMessage.setText(fileMessage.getName());
        } else {
            this.binding.tvMessage.setSingleLine(false);
            this.binding.tvMessage.setMaxLines(2);
            this.binding.tvMessage.setEllipsize(TextUtils.TruncateAt.END);
            this.binding.tvMessage.setTextAppearance(context, messageTextAppearance);
            this.binding.tvMessage.setText(message.getMessage());
            this.binding.ivIcon.setVisibility(View.GONE);
        }
    }

    @BindingAdapter("message")
    public static void drawMessage(MessagePreview view, BaseMessage message) {
        view.drawMessage(message);
    }

    private int getIconDrawable(@NonNull String mimeType) {
        int icon;
        if (mimeType.toLowerCase().contains(StringSet.image)) {
            if (mimeType.endsWith(StringSet.gif)) {
                icon = R.drawable.icon_gif;
            } else {
                icon = R.drawable.icon_photo;
            }
        } else if (mimeType.toLowerCase().contains(StringSet.video)) {
            icon = R.drawable.icon_play;
        } else if (mimeType.toLowerCase().contains(StringSet.audio)) {
            icon = R.drawable.icon_file_audio;
        } else {
            icon = R.drawable.icon_file_document;
        }
        return icon;
    }
}
