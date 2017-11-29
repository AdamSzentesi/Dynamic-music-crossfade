package music;

import static org.lwjgl.openal.AL10.*;


public class SoundSource
{
	private int sourceId;
	
	public SoundSource(boolean loop, boolean relative)
	{
		this.sourceId = alGenSources();
		if (loop)
		{
			alSourcei(sourceId, AL_LOOPING, AL_TRUE);
		}
		if (relative)
		{
			alSourcei(sourceId, AL_SOURCE_RELATIVE, AL_TRUE);
		}
	}
	
	public int getSourceId()
	{
		return this.sourceId;
	}
	
	public void setBuffer(int bufferId)
	{
		this.stop();
		alSourcei(this.sourceId, AL_BUFFER, bufferId);
	}
	
	public void setPosition(Vector3f position)
	{
		alSource3f(this.sourceId, AL_POSITION, position.x, position.y, position.z);
	}
	
	public void setVelocity(Vector3f velocity)
	{
		alSource3f(this.sourceId, AL_VELOCITY, velocity.x, velocity.y, velocity.z);
	}
	
	public void setGain(float gain)
	{
		alSourcef(this.sourceId, AL_GAIN, gain);
	}
	
	public void play()
	{
		alSourcePlay(this.sourceId);
	}
	
	public void pause()
	{
		alSourcePause(this.sourceId);
	}
	
	public void stop()
	{
		alSourceStop(this.sourceId);
	}
	
	public boolean isPlaying()
	{
		return alGetSourcei(this.sourceId, AL_SOURCE_STATE) == AL_PLAYING;
	}
	
	public void cleanUp()
	{
		alDeleteSources(this.sourceId);
  }
}
