package freelook.freelook.mixin;

import freelook.freelook.CameraOverriddenEntity;
import freelook.freelook.FreeLookMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements CameraOverriddenEntity {
    @Shadow
    public abstract void setAngles(float yaw, float pitch);

    @Shadow
    private float pitch;
    @Unique
    private float cameraPitch;

    @Unique
    private float cameraYaw;

    @Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
    public void changeCameraLookDirection(double xDelta, double yDelta, CallbackInfo ci) {
        //noinspection ConstantValue// IntelliJ is incorrect here, this code block is reachable
        if (FreeLookMod.isFreeLooking && (Object) this instanceof ClientPlayerEntity) {
            double pitchDelta = (yDelta * 0.15);
            double yawDelta = (xDelta * 0.15);

            this.cameraPitch = MathHelper.clamp(this.cameraPitch + (float) pitchDelta, -90.0f, 90.0f);
            this.cameraYaw += (float) yawDelta;
            this.setAngles((float)getAlternateViewYaw(),(float)getAlternateViewPitch());

            ci.cancel();

        }
    }

    double getAlternateViewYaw(){
        double camPitch = this.cameraPitch * Math.PI/180;
        double camYaw = this.cameraYaw * Math.PI/180;
        double Oiz = 0;
        double Oix = 0;
        double Oiy = 0.25;
        double Os = 0.25;
        double Dsxs = 0.375;
        double S = 0;
        if(Dsxs > 0) S=1;
        if(Dsxs < 0) S=-1;
        double Zf = Dsxs*Math.cos(this.cameraYaw) + Oiz;
        double Xf = Dsxs + S*Oix + Os;
        double M = 0;
        if(Zf > 0) M=0;
        if(Zf < 0) M=1;
        if(Zf == 0){
            if(Xf > 0) M=0;
            if(Xf < 0) M=1;
        }
        double theta = Math.atan((Xf*Math.tan(this.cameraYaw) - Oiz)/(Dsxs));
        theta = theta * 180 / Math.PI + 180*M;
        return theta;
    }

    double getAlternateViewPitch(){
        double camPitch = this.cameraPitch * Math.PI/180;
        double camYaw = this.cameraYaw * Math.PI/180;
        double initialOffsetY = 0;
        double initialOffsetX = 0;
        double initialOffsetZ = 0.25;
        double surfaceOffset = 0.25;
        double distanceFromSurface = 0.375;
        double sign = 0;
        if(distanceFromSurface > 0) sign=1;
        if(distanceFromSurface < 0) sign=-1;
        double finalXOffset = distanceFromSurface*Math.cos(this.cameraYaw) + initialOffsetY;
        double finalZOffset = distanceFromSurface + sign*initialOffsetX + surfaceOffset;
        double doRotation;
        if(finalXOffset > 0) doRotation=0;
        if(finalXOffset < 0) doRotation=1;
        if(finalXOffset == 0){
            if(finalZOffset > 0) doRotation=0;
            if(finalZOffset < 0) doRotation=1;
        }
        double theta = 0;
        theta = finalZOffset*Math.tan(this.cameraPitch);
        theta *= Math.abs(1/Math.cos(this.cameraYaw));
        theta -= initialOffsetZ;
        double denominator = distanceFromSurface * Math.abs(1/Math.cos(getAlternateViewPitch()));
        theta /= denominator;
        theta = Math.atan(theta);
        theta = theta * 180 / Math.PI;
        return theta;
    }

    @Override
    @Unique
    public float freelook$getCameraPitch() {
        return this.cameraPitch;
    }

    @Override
    @Unique
    public float freelook$getCameraYaw() {
        return this.cameraYaw;
    }

    @Override
    @Unique
    public void freelook$setCameraPitch(float pitch) {
        this.cameraPitch = pitch;
    }

    @Override
    @Unique
    public void freelook$setCameraYaw(float yaw) {
        this.cameraYaw = yaw;
    }
}