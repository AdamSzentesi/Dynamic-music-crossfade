package music;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import static org.lwjgl.openal.AL10.*;

public class SoundListener
{
	public SoundListener()
	{
		this(new Vector3f(0, 0, 0));
	}
	
	public SoundListener(Vector3f position)
	{
		alListener3f(AL_POSITION, position.x, position.y, position.z);
		alListener3f(AL_VELOCITY, 0, 0, 0);
	}
	
	public void setPosition(Vector3f position)
	{
		alListener3f(AL_POSITION, position.x, position.y, position.z);
	}
	
	public void setVelocity(Vector3f velocity)
	{
		alListener3f(AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}
	
	public void setOrientation(Vector3f direction, Vector3f up)
	{
		float data[] = new float[6];
		data[0] = direction.x;
		data[1] = direction.y;
		data[2] = direction.z;
		data[3] = up.x;
		data[4] = up.y;
		data[5] = up.z;
		FloatBuffer dataBuffer = (FloatBuffer)BufferUtils.createFloatBuffer(6).put(data).rewind();
		alListenerfv(AL_ORIENTATION, dataBuffer);
	}
}
