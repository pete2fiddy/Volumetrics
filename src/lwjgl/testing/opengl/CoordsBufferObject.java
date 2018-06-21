package lwjgl.testing.opengl;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;


public class CoordsBufferObject extends BufferObject {
    public float[][] points;
    
    public CoordsBufferObject(float[][] points, boolean normalized, int stride, int pointer) {
        super(GL_ARRAY_BUFFER, GL_FLOAT, points[0].length, normalized, stride, pointer);
        this.points = points;
    }
    
    @Override
    protected void sendBufferData(int usage) {
        float[] flatPoints = flattenPoints();
        FloatBuffer toBuffer = BufferUtils.createFloatBuffer(flatPoints.length);
        toBuffer.put(flatPoints).flip();
        glBufferData(getTarget(), toBuffer, usage);
    }
    
    private float[] flattenPoints(){
        float[] flatPoints = new float[points.length * points[0].length];
        for(int i = 0; i < points.length; i++) {
            for(int j = 0; j < points[i].length; j++) {
                flatPoints[i*points[i].length + j] = points[i][j];
            }
        }
        return flatPoints;
    }

}