/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.screen;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.desertstormfront.app.GameApplication;
import com.desertstormfront.audio.MusicPlaylist;
import com.desertstormfront.config.GameConfig;
import com.desertstormfront.config.TouchDeviceFlags;
import com.desertstormfront.screen.BaseScreen;
import com.desertstormfront.screen.MainMenuScreen;
import com.desertstormfront.screen.MenuActionListener;
import com.desertstormfront.screen.MenuDialogListener;
import com.desertstormfront.screen.PurchaseCallback;
import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.BatchRenderer;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Checkbox;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.ListBox;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.ScrollPane;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.Widget;
import com.desertstormfront.ui.hud.GuiAssets;
import com.desertstormfront.ui.hud.MessageDialog;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.log.OsfLog;
import com.noblemaster.lib.math.MathHelper;

public abstract class BaseMenuScreen
extends BaseScreen {
    private long showTime;
    private long lastFrameTime;
    private boolean active = true;
    private BatchRenderer backgroundRenderer;
    private Image[] backgroundPlates;
    private Image logoImage;
    private Image[] curtainImages;
    private Image[] floatingImages;
    private float[][] floatingMotion;
    private BatchRenderer hudRenderer;
    private Container menuContainer;
    private Container backButtonContainer;
    private Label backLabel;
    private Label backLabelShadow;
    private Container unusedMenuItem;
    private Container websiteItem;
    private Container forumItem;
    private Container purchaseItem;
    private Container contentContainer;
    private boolean wasTouchDown;
    private ActionListener menuActionListener;
    private BaseScreen nextScreen;
    private int pendingAction;
    private MessageDialog messageDialog;
    private ActionListener dialogListener;
    private MusicPlaylist musicPlaylist;

    protected BaseMenuScreen() {
        this(null);
    }

    protected BaseMenuScreen(BaseMenuScreen baseMenuScreen) {
        if (baseMenuScreen != null) {
            baseMenuScreen.active = false;
            this.showTime = baseMenuScreen.showTime;
            this.lastFrameTime = baseMenuScreen.lastFrameTime;
            this.backgroundRenderer = baseMenuScreen.backgroundRenderer;
            this.backgroundPlates = baseMenuScreen.backgroundPlates;
            this.logoImage = baseMenuScreen.logoImage;
            this.curtainImages = baseMenuScreen.curtainImages;
            this.floatingImages = baseMenuScreen.floatingImages;
            this.floatingMotion = baseMenuScreen.floatingMotion;
            this.hudRenderer = baseMenuScreen.hudRenderer;
            this.menuContainer = baseMenuScreen.menuContainer;
            this.backButtonContainer = baseMenuScreen.backButtonContainer;
            this.backLabelShadow = baseMenuScreen.backLabelShadow;
            this.backLabel = baseMenuScreen.backLabel;
            this.unusedMenuItem = baseMenuScreen.unusedMenuItem;
            this.websiteItem = baseMenuScreen.websiteItem;
            this.forumItem = baseMenuScreen.forumItem;
            this.purchaseItem = baseMenuScreen.purchaseItem;
            this.contentContainer = baseMenuScreen.contentContainer;
            this.messageDialog = baseMenuScreen.messageDialog;
            this.musicPlaylist = baseMenuScreen.musicPlaylist;
        }
    }

    @Override
    public BaseScreen update() {
        boolean i1 = this.isTouchDown();
        int i2 = MathHelper.round((float)this.getTouchX() * GameApplication.scale);
        int i3 = MathHelper.round((float)this.getTouchY() * GameApplication.scale);
        if (!(i1 || this.wasTouchDown || TouchDeviceFlags.isTouchDevice())) {
            i2 = -1;
            i3 = -1;
        }
        this.wasTouchDown = i1;
        if (this.messageDialog.isVisible()) {
            i2 = (int)((float)i2 - this.hudRenderer.getRootWidget().getX());
            i3 = (int)((float)i3 - this.hudRenderer.getRootWidget().getY());
            this.messageDialog.handleInput(this.dialogListener, i2, i3, i1);
        } else {
            this.hudRenderer.handleInput(this.menuActionListener, i2, i3, i1);
        }
        return this.nextScreen;
    }

    @Override
    public void show() {
        this.showTime = System.nanoTime();
        this.lastFrameTime = System.nanoTime();
        this.nextScreen = null;
    }

    @Override
    public void createGlResources() {
        Object object;
        Object object2;
        Object object3;
        Object object4;
        Widget widget;
        Container container;
        GL20 gL20 = Gdx.gl20;
        gL20.glBlendFunc(770, 771);
        gL20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gL20.glEnable(3042);
        gL20.glDepthFunc(515);
        gL20.glClearDepthf(1.0f);
        gL20.glDisable(2929);
        gL20.glDisable(3024);
        gL20.glDisable(2960);
        gL20.glDisable(2884);
        gL20.glActiveTexture(33984);
        if (this.backgroundRenderer == null) {
            this.backgroundRenderer = this.getMenuBackgroundRenderer();
            container = new Container();
            widget = new Image(new TextureRegion(0, 0, 2048, 1601));
            container.addChild(widget);
            object4 = new TextureRegion(0, 1601, 114, 104);
            object3 = new TextureRegion(114, 1601, 102, 104);
            object2 = new TextureRegion(0, 1705, 216, 207);
            this.floatingImages = new Image[5];
            this.floatingImages[0] = new Image((TextureRegion)object4);
            this.floatingImages[0].setX(-150.0f);
            this.floatingImages[0].setY(150.0f);
            container.addChild(this.floatingImages[0]);
            this.floatingImages[1] = new Image((TextureRegion)object4);
            this.floatingImages[1].setX(-300.0f);
            this.floatingImages[1].setY(265.0f);
            container.addChild(this.floatingImages[1]);
            this.floatingImages[2] = new Image((TextureRegion)object4);
            this.floatingImages[2].setX(-600.0f);
            this.floatingImages[2].setY(385.0f);
            container.addChild(this.floatingImages[2]);
            this.floatingImages[3] = new Image((TextureRegion)object2);
            this.floatingImages[3].setX(-150.0f);
            this.floatingImages[3].setY(200.0f);
            container.addChild(this.floatingImages[3]);
            this.floatingImages[4] = new Image((TextureRegion)object3);
            this.floatingImages[4].setX(-600.0f);
            this.floatingImages[4].setY(582.0f);
            container.addChild(this.floatingImages[4]);
            this.floatingMotion = new float[][]{{36.0f, -150.0f, 0.0f}, {41.0f, -150.0f, 0.0f}, {48.0f, -150.0f, 0.0f}, {255.0f, -150.0f, 0.0f}, {210.0f, -150.0f, 0.0f}};
            this.logoImage = this.getMarket().getName().equals("iWin") ? new Image(new TextureRegion(1241, 1601, 273, 273)) : new Image(new TextureRegion(968, 1601, 273, 273));
            container.addChild(this.logoImage);
            object = new TextureRegion(968, 1874, 30, 144);
            this.curtainImages = new Image[15];
            int n = 0;
            while (n < this.curtainImages.length) {
                this.curtainImages[n] = new Image((TextureRegion)object);
                container.addChild(this.curtainImages[n]);
                ++n;
            }
            TextureRegion textureRegion = new TextureRegion(216, 1601, 752, 447);
            this.backgroundPlates = new Image[8];
            this.backgroundPlates[0] = new Image(textureRegion);
            this.backgroundPlates[0].setX(0.0f);
            this.backgroundPlates[0].setY(200.0f);
            this.backgroundPlates[0].setWidth(textureRegion.width);
            this.backgroundPlates[0].setHeight(textureRegion.height);
            container.addChild(this.backgroundPlates[0]);
            this.backgroundPlates[1] = new Image(textureRegion);
            this.backgroundPlates[1].setX(1250.0f);
            this.backgroundPlates[1].setY(0.0f);
            this.backgroundPlates[1].setWidth(-textureRegion.width);
            this.backgroundPlates[1].setHeight(textureRegion.height);
            container.addChild(this.backgroundPlates[1]);
            this.backgroundPlates[2] = new Image(textureRegion);
            this.backgroundPlates[2].setX(1024.0f);
            this.backgroundPlates[2].setY(150.0f);
            this.backgroundPlates[2].setWidth(textureRegion.width);
            this.backgroundPlates[2].setHeight(textureRegion.height);
            container.addChild(this.backgroundPlates[2]);
            this.backgroundPlates[3] = new Image(textureRegion);
            this.backgroundPlates[3].setX(2300.0f);
            this.backgroundPlates[3].setY(50.0f);
            this.backgroundPlates[3].setWidth(-textureRegion.width);
            this.backgroundPlates[3].setHeight(textureRegion.height);
            container.addChild(this.backgroundPlates[3]);
            this.backgroundPlates[4] = new Image(textureRegion);
            this.backgroundPlates[4].setX(300.0f);
            this.backgroundPlates[4].setY(700.0f);
            this.backgroundPlates[4].setWidth(textureRegion.width);
            this.backgroundPlates[4].setHeight(textureRegion.height);
            container.addChild(this.backgroundPlates[4]);
            this.backgroundPlates[5] = new Image(textureRegion);
            this.backgroundPlates[5].setX(1150.0f);
            this.backgroundPlates[5].setY(0.0f);
            this.backgroundPlates[5].setWidth(-textureRegion.width);
            this.backgroundPlates[5].setHeight(textureRegion.height);
            container.addChild(this.backgroundPlates[5]);
            this.backgroundPlates[6] = new Image(textureRegion);
            this.backgroundPlates[6].setX(1224.0f);
            this.backgroundPlates[6].setY(650.0f);
            this.backgroundPlates[6].setWidth(textureRegion.width);
            this.backgroundPlates[6].setHeight(textureRegion.height);
            container.addChild(this.backgroundPlates[6]);
            this.backgroundPlates[7] = new Image(textureRegion);
            this.backgroundPlates[7].setX(2100.0f);
            this.backgroundPlates[7].setY(950.0f);
            this.backgroundPlates[7].setWidth(-textureRegion.width);
            this.backgroundPlates[7].setHeight(textureRegion.height);
            container.addChild(this.backgroundPlates[7]);
            this.backgroundRenderer.setRootWidget(container);
            container.pack();
        }
        if (this.hudRenderer == null) {
            this.hudRenderer = this.getHudRenderer();
            container = new Container();
            this.menuContainer = new Container();
            container.addChild(this.menuContainer);
            this.contentContainer = new Container();
            container.addChild(this.contentContainer);
            this.hudRenderer.setRootWidget(container);
            widget = new Container();
            this.menuContainer.addChild(widget);
            this.unusedMenuItem = this.createMenuItemContainer(-1, null);
            this.unusedMenuItem.setY(0.0f);
            ((Container)widget).addChild(this.unusedMenuItem);
            this.websiteItem = this.createMenuItemContainer(-1, Messages.get("Website[i18n]: Website"));
            this.websiteItem.setY(0.0f);
            ((Container)widget).addChild(this.websiteItem);
            this.forumItem = this.createMenuItemContainer(-2, Messages.get("Forums[i18n]: Forums"));
            this.forumItem.setY(83.0f);
            ((Container)widget).addChild(this.forumItem);
            this.purchaseItem = this.createMenuItemContainer(-9999, Messages.get("Purchase[i18n]: Purchase"));
            this.purchaseItem.setY(83.0f);
            ((Container)widget).addChild(this.purchaseItem);
            this.backButtonContainer = new Container();
            this.backButtonContainer.setY(166.0f);
            ((Container)widget).addChild(this.backButtonContainer);
            object4 = new ButtonStyle(new int[]{333, 333, 333, 333}, new int[]{396, 442, 488, 534}, 123, 45);
            object3 = new Image(new TextureRegion(0, 232, 161, 71));
            this.backButtonContainer.addChild((Widget)object3);
            object2 = new Container();
            this.backLabel = new Label(GuiAssets.getDefaultFont());
            this.backLabel.setX(1.0f);
            this.backLabel.setY(17.0f);
            this.backLabel.a(-1);
            this.backLabel.setMaxWidth(((ButtonStyle)object4).width);
            this.backLabel.setAlign(Align.CENTER);
            ((Container)object2).addChild(this.backLabel);
            this.backLabelShadow = new Label(GuiAssets.getDefaultFont());
            this.backLabelShadow.setX(0.0f);
            this.backLabelShadow.setY(16.0f);
            this.backLabelShadow.a(-16777216);
            this.backLabelShadow.setMaxWidth(((ButtonStyle)object4).width);
            this.backLabelShadow.setAlign(Align.CENTER);
            ((Container)object2).addChild(this.backLabelShadow);
            object = new Button((ButtonStyle)object4, (Widget)object2);
            ((Widget)object).setData(0);
            ((Widget)object).setX(19.0f);
            ((Widget)object).setY(12.0f);
            this.backButtonContainer.addChild((Widget)object);
            ((Container)widget).pack();
            if (this.messageDialog == null) {
                this.messageDialog = new MessageDialog();
                this.messageDialog.setVisible(false);
            }
            container.addChild(this.messageDialog);
        } else {
            this.contentContainer.clearChildren();
        }
        this.unusedMenuItem.setVisible(false);
        this.websiteItem.setVisible(false);
        this.forumItem.setVisible(false);
        this.purchaseItem.setVisible(false);
        boolean bl = this.isBackEnabled(this.contentContainer);
        this.backLabel.setText(Messages.get(bl ? "Back[i18n]: Back" : "Exit[i18n]: Exit"));
        this.backLabelShadow.setText(this.backLabel.getText());
        this.backButtonContainer.setVisible(bl || Gdx.app.getType() != Application.ApplicationType.iOS);
        this.menuActionListener = new MenuActionListener(this);
        this.dialogListener = new MenuDialogListener(this);
        this.hudRenderer.getRootWidget().pack();
        if (this.musicPlaylist == null) {
            this.musicPlaylist = new MusicPlaylist(GameConfig.getDataFileResolver().getInternal(String.valueOf(GameConfig.getConfigDir()) + "music_inmenu.mp3"));
            this.musicPlaylist.resume();
        }
    }

    protected abstract boolean isBackEnabled(Container var1);

    protected abstract void createContent(Container var1);

    protected void setupScrollPane(Container container, NinePatchImage ninePatchImage, ScrollPane scrollPane) {
        this.setupScrollPane(container, ninePatchImage, scrollPane, 0);
    }

    protected void setupScrollPane(Container container, NinePatchImage ninePatchImage, ScrollPane scrollPane, int i4) {
        this.setupScrollPane(container, ninePatchImage, scrollPane, i4, true);
    }

    protected void setupScrollPane(Container container, NinePatchImage ninePatchImage, ScrollPane scrollPane, int i4, boolean bl) {
        this.setupScrollPane(container, ninePatchImage, (Widget)scrollPane, i4);
        if (bl) {
            scrollPane.getContent().setY(0.0f);
        }
    }

    private void setupScrollPane(Container container, NinePatchImage ninePatchImage, Widget widget, int i4) {
        float f5 = container.getWidth();
        float f6 = container.getHeight();
        int i7 = 20 + (f5 < 800.0f ? 0 : ((int)f5 - 800) / 4);
        ninePatchImage.setX(i7);
        ninePatchImage.setY((float)(i7 / 2 + 10 + i4));
        ninePatchImage.setWidth(f5 - (float)i7 - (float)i7);
        ninePatchImage.setHeight(f6 - (float)i4 - 20.0f - (float)i7);
        int[] nArray = GuiAssets.getDialogContentInsets();
        widget.setX(ninePatchImage.getX() + (float)nArray[0]);
        widget.setY(ninePatchImage.getY() + (float)nArray[1]);
        widget.setWidth(ninePatchImage.getWidth() - (float)nArray[0] - (float)nArray[2]);
        widget.setHeight(ninePatchImage.getHeight() - (float)nArray[1] - (float)nArray[3]);
    }

    protected abstract BaseScreen handleWidgetAction(Widget var1);

    protected final void showDialog(int i1, String string, String string2, String[] stringArray) {
        this.pendingAction = i1;
        this.messageDialog.setTitle(string);
        this.messageDialog.setText(string2);
        this.messageDialog.setLines(stringArray);
        this.messageDialog.setVisible(true);
    }

    protected abstract BaseScreen handleAction(int var1, int var2);

    protected BaseScreen showPurchaseDialog(int i1) {
        String string = GameConfig.getBuyFullVersionText();
        String string2 = GameConfig.getBuyFullVersionDetails();
        String[] stringArray = this.getMarket().hasUrl() || this.getLicense().d() ? new String[]{Messages.get("Purchase[i18n]: Purchase"), Messages.get("Cancel[i18n]: Cancel")} : new String[]{Messages.get("OK[i18n]: OK")};
        this.showDialog(i1, string, string2, stringArray);
        return null;
    }

    protected BaseScreen handlePurchaseAction(int i1) {
        switch (i1) {
            case 0: {
                if (this.getLicense().d()) {
                    this.getLicense().check(new PurchaseCallback(this));
                    return new MainMenuScreen(this);
                }
                if (this.getMarket().hasUrl()) {
                    this.openUrl(this.getMarket().getUrl());
                    return null;
                }
                return null;
            }
            case 1: {
                return null;
            }
        }
        OsfLog.error("Action not implemented: " + i1);
        return null;
    }

    protected final Label createLabel(int i1, int i2) {
        return this.createLabel(i1, i2, null);
    }

    protected final Label createLabel(int i1, int i2, String string) {
        return this.createLabel(i1, i2, string, 0);
    }

    protected final Label createLabel(int i1, int i2, String string, int i4) {
        Label label = new Label(GuiAssets.getDefaultFont());
        label.setX((float)i1);
        label.setY((float)i2);
        label.setText(string);
        label.setMaxWidth(i4);
        return label;
    }

    protected final Button createButton(int i1, int i2, int i3, String string) {
        Container container = new Container();
        Label label = new Label(GuiAssets.getDefaultFont());
        label.setX(1.0f);
        label.setY(17.0f);
        label.a(-1);
        label.setText(string);
        label.setMaxWidth(GuiAssets.getDialogButtonStyle().width);
        label.setAlign(Align.CENTER);
        container.addChild(label);
        Label label2 = new Label(GuiAssets.getDefaultFont());
        label2.setX(0.0f);
        label2.setY(16.0f);
        label2.a(-16777216);
        label2.setText(string);
        label2.setMaxWidth(GuiAssets.getDialogButtonStyle().width);
        label2.setAlign(Align.CENTER);
        container.addChild(label2);
        Button button = new Button(GuiAssets.getMenuButtonStyle(), container);
        button.setData(i1);
        button.setX(i2);
        button.setY((float)i3);
        return button;
    }

    protected Checkbox createCheckbox(int i1, int i2, int i3) {
        Checkbox checkbox = new Checkbox(GuiAssets.getCheckboxStyle());
        checkbox.setData(i1);
        checkbox.setX(i2);
        checkbox.setY((float)i3);
        return checkbox;
    }

    protected ListBox createListBox(int i1, int i2, int i3) {
        ListBox listBox = new ListBox(GuiAssets.getMenuListBoxItemStyle(), GuiAssets.getMenuListBoxSelectionStyle(), GuiAssets.getDefaultFont());
        listBox.setLabelAlign(-16732433);
        listBox.setData((Object)i1);
        listBox.setX((float)i2);
        listBox.setY((float)i3);
        return listBox;
    }

    protected void showWebsiteAndForumButtons() {
        if (!(this.getMarket().getName().equals("ChinaMobile") || this.getMarket().getName().equals("iWin") || this.getMarket().getName().equals("JoyMoa"))) {
            this.websiteItem.setVisible(true);
        }
        if (!this.getMarket().getName().equals("ChinaMobile") && !this.getMarket().getName().equals("iWin")) {
            this.forumItem.setVisible(true);
        }
    }

    protected void showPurchaseButton() {
        if (this.getLicense().d()) {
            this.purchaseItem.setVisible(true);
        }
    }

    private Container createMenuItemContainer(int i1, String string) {
        Container container = new Container();
        ButtonStyle buttonStyle = new ButtonStyle(new int[]{584, 584, 584, 584}, new int[]{810, 856, 902, 948}, 123, 45);
        Image image = new Image(new TextureRegion(0, 232, 161, 71));
        container.addChild(image);
        Container container2 = new Container();
        Label label = new Label(GuiAssets.getDefaultFont());
        label.setX(1.0f);
        label.setY(17.0f);
        label.a(-1);
        label.setText(string);
        label.setMaxWidth(buttonStyle.width);
        label.setAlign(Align.CENTER);
        container2.addChild(label);
        Label label2 = new Label(GuiAssets.getDefaultFont());
        label2.setX(0.0f);
        label2.setY(16.0f);
        label2.a(-16777216);
        label2.setText(string);
        label2.setMaxWidth(buttonStyle.width);
        label2.setAlign(Align.CENTER);
        container2.addChild(label2);
        Button button = new Button(buttonStyle, container2);
        button.setData(i1);
        button.setX(19.0f);
        button.setY(12.0f);
        container.addChild(button);
        return container;
    }

    @Override
    protected void layout() {
        GL20 gL20 = Gdx.gl20;
        int i2 = this.getWidth();
        int i3 = this.getHeight();
        gL20.glViewport(0, 0, i2, i3);
        this.backgroundRenderer.setViewportSize(i2, i3);
        int i4 = i2 < 800 ? 0 : (i2 - 800) / 16;
        float f5 = 100 + i4;
        float f6 = 90 + i4;
        this.logoImage.setX(f5 - this.logoImage.getWidth() / 2.0f);
        this.logoImage.setY(f6 - this.logoImage.getHeight() / 2.0f);
        int i7 = 0;
        while (i7 < this.curtainImages.length) {
            this.curtainImages[i7].setX(f5 - this.curtainImages[i7].getWidth() / 2.0f);
            this.curtainImages[i7].setY(f6 + this.logoImage.getHeight() / 2.0f + (float)i7 * this.curtainImages[i7].getHeight());
            ++i7;
        }
        i7 = 0;
        while (i7 < this.floatingMotion.length) {
            this.floatingMotion[i7][2] = i2 + 150;
            ++i7;
        }
        this.messageDialog.setX(((float)i2 - this.messageDialog.getWidth()) / 2.0f);
        this.messageDialog.setY(((float)i3 - this.messageDialog.getHeight()) / 2.0f);
        i7 = 18 + i4;
        this.hudRenderer.setViewportSize(i2, i3);
        this.menuContainer.setX(i7);
        this.menuContainer.setY((float)i3 - this.menuContainer.getHeight() - (float)i7);
        this.contentContainer.setX((float)i7 + this.menuContainer.getWidth() + (float)i7);
        this.contentContainer.setY(0.0f);
        this.contentContainer.setWidth((float)i2 - this.contentContainer.getX() - (float)i7);
        this.contentContainer.setHeight(i3);
        this.createContent(this.contentContainer);
    }

    @Override
    public void render() {
        float f9;
        GL20 gL20 = Gdx.gl20;
        gL20.glClear(16384);
        long l2 = System.nanoTime();
        long l4 = l2 - this.lastFrameTime;
        this.lastFrameTime = l2;
        float f6 = (float)l4 / 5.0E7f;
        int i7 = 0;
        while (i7 < this.backgroundPlates.length) {
            Image image = this.backgroundPlates[i7];
            f9 = image.getX() - f6;
            float f = image.getWidth() > 0.0f ? image.getWidth() : 0.0f;
            if (f9 + f < 0.0f) {
                f9 = 2048.0f + (image.getWidth() > 0.0f ? 0.0f : -image.getWidth());
            }
            image.setX(f9);
            ++i7;
        }
        i7 = 0;
        while (i7 < this.floatingMotion.length) {
            float f = this.floatingMotion[i7][0];
            f9 = this.floatingMotion[i7][1];
            float f10 = this.floatingMotion[i7][2];
            float f11 = f * (float)l4 / 1.0E9f;
            Image image = this.floatingImages[i7];
            float f13 = image.getX() + f11;
            if (f > 0.0f) {
                if (f13 >= f10) {
                    f13 += this.floatingImages[i7].getWidth();
                    this.floatingImages[i7].setWidth(-this.floatingImages[i7].getWidth());
                    this.floatingMotion[i7][0] = -f;
                }
            } else if (f13 <= f9) {
                f13 += this.floatingImages[i7].getWidth();
                this.floatingImages[i7].setWidth(-this.floatingImages[i7].getWidth());
                this.floatingMotion[i7][0] = -f;
            }
            image.setX(f13);
            ++i7;
        }
        this.backgroundRenderer.render();
        this.hudRenderer.render();
        this.musicPlaylist.update();
        super.render();
    }

    @Override
    public void pause() {
        if (this.active) {
            this.hudRenderer = null;
            this.disposeMenuBackgroundRenderer();
            this.backgroundRenderer = null;
            this.musicPlaylist.dispose();
            this.musicPlaylist = null;
        }
    }

    @Override
    public void dispose() {
    }

    static /* synthetic */ void setNextScreen(BaseMenuScreen baseMenuScreen, BaseScreen baseScreen) {
        baseMenuScreen.nextScreen = baseScreen;
    }

    static /* synthetic */ MessageDialog getMessageDialog(BaseMenuScreen baseMenuScreen) {
        return baseMenuScreen.messageDialog;
    }

    static /* synthetic */ int getPendingAction(BaseMenuScreen baseMenuScreen) {
        return baseMenuScreen.pendingAction;
    }
}

