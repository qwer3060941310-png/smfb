/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.ui.hud;

import com.desertstormfront.ui.ActionListener;
import com.desertstormfront.ui.Align;
import com.desertstormfront.ui.BitmapFont;
import com.desertstormfront.ui.Button;
import com.desertstormfront.ui.ButtonStyle;
import com.desertstormfront.ui.Container;
import com.desertstormfront.ui.Image;
import com.desertstormfront.ui.Label;
import com.desertstormfront.ui.NinePatch;
import com.desertstormfront.ui.NinePatchImage;
import com.desertstormfront.ui.SpriteBatch;
import com.desertstormfront.ui.TextureRegion;
import com.desertstormfront.ui.hud.NextClickListener;
import com.desertstormfront.ui.hud.TutorialListener;
import com.noblemaster.lib.i18n.Messages;
import com.noblemaster.lib.script.ScriptStep;
import com.noblemaster.lib.script.ScriptStepList;
import com.noblemaster.lib.script.StepSequencePlayer;

public final class TutorialHintBox
extends Container {
    private StepSequencePlayer stepPlayer;
    private ActionListener nextListener;
    private Container contentContainer;
    private NinePatchImage backgroundPatch;
    private Label textLabel;
    private Button nextButton;
    private Image pointerImage;
    private float pointerBaseY;
    private float pointerBobOffset;
    private boolean bobbingUp;
    private long lastFrameTime;

    public TutorialHintBox() {
        this.stepPlayer = null;
        this.setVisible(false);
    }

    public TutorialHintBox(String[][] stringArray, BitmapFont bitmapFont, NinePatch ninePatch, ButtonStyle buttonStyle, TextureRegion textureRegion) {
        this.stepPlayer = new StepSequencePlayer(new ScriptStepList(stringArray));
        this.pointerImage = new Image(textureRegion);
        this.pointerImage.pack();
        this.pointerBaseY = 0.0f;
        this.pointerBobOffset = 0.0f;
        this.bobbingUp = true;
        this.lastFrameTime = System.nanoTime();
        this.addChild(this.pointerImage);
        this.pointerImage.setVisible(false);
        this.contentContainer = new Container();
        this.addChild(this.contentContainer);
        this.backgroundPatch = new NinePatchImage(ninePatch);
        this.contentContainer.addChild(this.backgroundPatch);
        this.textLabel = new Label(bitmapFont);
        this.textLabel.a(-14671840);
        this.contentContainer.addChild(this.textLabel);
        Label label = new Label(bitmapFont);
        label.setX(17.0f);
        label.setY(39.0f);
        label.a(-14671840);
        label.setText(Messages.get("Next[i18n]: Next"));
        label.setMaxWidth(buttonStyle.width);
        label.setAlign(Align.CENTER);
        this.nextButton = new Button(buttonStyle, label);
        this.nextButton.setData(0);
        this.contentContainer.addChild(this.nextButton);
        this.stepPlayer.addListener(new TutorialListener(this));
        this.nextListener = new NextClickListener(this);
        this.applyCurrentStep();
    }

    public final void setSize(int i1, int i2) {
        this.setWidth(i1);
        this.setHeight(i2);
        this.layoutByAnchor();
    }

    public final boolean isWaitingForConfirmation() {
        if (this.stepPlayer != null) {
            ScriptStep scriptStep = this.stepPlayer.getCurrentStep();
            if (scriptStep != null) {
                return scriptStep.getExpectedInput() == null;
            }
            return false;
        }
        return false;
    }

    public final String getActionCode() {
        if (this.stepPlayer != null) {
            ScriptStep scriptStep = this.stepPlayer.getCurrentStep();
            if (scriptStep != null) {
                return scriptStep.getActionCode();
            }
            return null;
        }
        return null;
    }

    public final void handlePointer(float f1, float f2, boolean bl) {
        if (this.stepPlayer != null) {
            this.handleInput(this.nextListener, f1, f2, bl);
        }
    }

    public final void notifyStepEvent(String string) {
        if (this.stepPlayer != null) {
            this.stepPlayer.advance(string);
        }
    }

    public final void showPointerAt(int i1, int i2) {
        this.pointerImage.setVisible(true);
        if (this.pointerImage.getX() != (float)i1 - this.pointerImage.getWidth() / 2.0f || this.pointerBaseY != (float)i2 - this.pointerImage.getHeight()) {
            this.pointerImage.setX((float)i1 - this.pointerImage.getWidth() / 2.0f);
            this.pointerImage.setY((float)i2 - this.pointerImage.getHeight());
            this.pointerBaseY = this.pointerImage.getY();
        }
    }

    public final void hidePointer() {
        this.pointerImage.setVisible(false);
    }

    private void applyCurrentStep() {
        ScriptStep scriptStep;
        ScriptStep scriptStep2 = scriptStep = this.stepPlayer != null ? this.stepPlayer.getCurrentStep() : null;
        if (scriptStep != null) {
            String string = Messages.get(scriptStep.getTextKey());
            this.textLabel.setText(string);
            this.nextButton.setVisible(scriptStep.getExpectedInput() == null);
            int i3 = 10;
            int i4 = 480;
            this.textLabel.setMaxWidth(i4);
            this.textLabel.setX((float)i3);
            this.textLabel.setY((float)i3);
            this.textLabel.pack();
            int i5 = (int)this.textLabel.getHeight();
            if (this.nextButton.isVisible()) {
                this.nextButton.setX(i3 + i4 - this.nextButton.getStyle().width + 6);
                this.nextButton.setY((float)(i3 + i5 - 30));
                this.backgroundPatch.setHeight(i3 + i5 + 28 + i3);
            } else {
                this.backgroundPatch.setHeight(i3 + i5 + i3 - 3);
            }
            this.backgroundPatch.setWidth(i3 + i4 + i3);
            this.contentContainer.pack();
            this.contentContainer.setWidth(this.backgroundPatch.getWidth());
            this.contentContainer.setHeight(this.backgroundPatch.getHeight());
            this.setVisible(true);
            this.layoutByAnchor();
        } else {
            this.setVisible(false);
        }
    }

    private void layoutByAnchor() {
        ScriptStep scriptStep;
        ScriptStep scriptStep2 = scriptStep = this.stepPlayer != null ? this.stepPlayer.getCurrentStep() : null;
        if (scriptStep != null) {
            float f2 = this.contentContainer.getWidth();
            float f3 = this.contentContainer.getHeight();
            float f4 = 0.0f;
            float f5 = 0.0f;
            String[] stringArray = scriptStep.getPositionSpec().split(":");
            int i7 = Integer.parseInt(stringArray[1]);
            if (stringArray[0].equals("L")) {
                f4 = i7;
            } else if (stringArray[0].equals("C")) {
                f4 = (this.getWidth() - f2) / 2.0f + (float)i7;
            } else if (stringArray[0].equals("R")) {
                f4 = this.getWidth() - f2 + (float)i7;
            }
            int i8 = Integer.parseInt(stringArray[3]);
            if (stringArray[2].equals("T")) {
                f5 = i8;
            } else if (stringArray[2].equals("C")) {
                f5 = (this.getHeight() - f3) / 2.0f + (float)i8;
            } else if (stringArray[2].equals("CT")) {
                f5 = this.getHeight() / 2.0f + (float)i8;
            } else if (stringArray[2].equals("CB")) {
                f5 = this.getHeight() / 2.0f - f3 + (float)i8;
            } else if (stringArray[2].equals("B")) {
                f5 = this.getHeight() - f3 + (float)i8;
            }
            this.contentContainer.setX(f4);
            this.contentContainer.setY(f5);
        }
    }

    @Override
    protected void draw(SpriteBatch spriteBatch, float f2, float f3) {
        if (this.isVisible()) {
            long l4 = System.nanoTime();
            this.pointerBobOffset += (float)(this.bobbingUp ? -1 : 1) * ((float)(l4 - this.lastFrameTime) * 6.0E-8f);
            if (this.pointerBobOffset < -10.0f) {
                this.pointerBobOffset = -10.0f;
                this.bobbingUp = false;
            } else if (this.pointerBobOffset >= 0.0f) {
                this.pointerBobOffset = 0.0f;
                this.bobbingUp = true;
            }
            this.pointerImage.setY(this.pointerBaseY + this.pointerBobOffset);
            this.lastFrameTime = l4;
            super.draw(spriteBatch, f2, f3);
        }
    }

    static /* synthetic */ void refreshFromListener(TutorialHintBox tutorialHintBox) {
        tutorialHintBox.applyCurrentStep();
    }

    static /* synthetic */ StepSequencePlayer getStepPlayerFromListener(TutorialHintBox tutorialHintBox) {
        return tutorialHintBox.stepPlayer;
    }
}

