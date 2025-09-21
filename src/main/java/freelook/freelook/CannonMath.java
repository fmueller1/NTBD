package freelook.freelook;

import org.joml.Vector2d;

public class CannonMath {
    final int cannonRot = 1;
    final double initialOffsetX = 0;
    final double initialOffsetY = 0.25;
    final double initialOffsetZ = 0;
    final double surfaceOffset = -0.25;
    final double distanceFromSurface = 0.375;
    //non-final variables are defined in the constructor as their values depend on non constants.
    double sign = 0;
    double finalXOffset = 0;
    double doRotation;

    public CannonMath(){
        if(distanceFromSurface > 0) sign=1;
        if(distanceFromSurface < 0) sign=-1;

        finalXOffset = distanceFromSurface + sign*surfaceOffset + initialOffsetX;
    }

    private double getDoRotation(double cameraYaw){
        if(cameraYaw <= -Math.PI/2d) return -1.0;
        if(cameraYaw >= Math.PI/2d) return 1.0;
        return 0.0;
    }

    private double getRotatedYaw(double cameraYaw){
        cameraYaw += Math.PI*cannonRot/2;
        while(cameraYaw >= Math.PI){
            cameraYaw -= Math.PI;
        }
        return cameraYaw;
    }

    public double getNewCameraYaw(double cameraPitch, double cameraYaw){
        doRotation = getDoRotation(cameraYaw);
        double theta = Math.atan((finalXOffset*Math.tan(cameraYaw) - initialOffsetZ)/(distanceFromSurface));
        theta = Math.toDegrees(theta);
        theta += 180d*doRotation;
        theta = Math.toRadians(theta);
        return theta;
    }

    public double getNewCameraPitch(double cameraPitch, double cameraYaw){
        double theta = finalXOffset*Math.tan(cameraPitch);
        theta *= Math.abs(1d/Math.cos(cameraYaw));
        theta -= initialOffsetY;
        double denominator = distanceFromSurface * Math.abs(1d/Math.cos(getNewCameraYaw(cameraPitch, cameraYaw)));
        theta /= denominator;
        theta = Math.atan(theta);
        return theta;
    }

    public Vector2d getNewCameraDirection(double cameraPitch, double cameraYaw){
        Vector2d vec = new Vector2d();
        double rotatedYaw = getRotatedYaw(cameraYaw);
        vec.x = getNewCameraYaw(cameraPitch, rotatedYaw);
        vec.y = getNewCameraPitch(cameraPitch, vec.x);
        return vec;
    }
}