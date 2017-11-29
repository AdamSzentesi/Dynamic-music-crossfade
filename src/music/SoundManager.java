package music;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static music.SuperUtils.say;
import org.lwjgl.openal.AL;
import static org.lwjgl.openal.AL10.*;
import org.lwjgl.openal.ALC;
import static org.lwjgl.openal.ALC10.*;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

public class SoundManager
{
	private long device;
	private long context;
	private SoundListener soundListener;
	private Map<String, SoundBuffer> soundBuffers;
	private List<SoundSource> soundSources;
		
	public SoundManager()
	{
		//get sound device
		String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		this.device = alcOpenDevice(defaultDeviceName);
		
		//create audio context
		int[] attributes = {0};
		this.context = alcCreateContext(this.device, attributes);
		alcMakeContextCurrent(this.context);
		
		//get device capabilities
		ALCCapabilities alcCapabilities = ALC.createCapabilities(this.device);
		ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
		
		if(alCapabilities.OpenAL10)
		{
			say("SoundManager: device " + defaultDeviceName + " fully capable of OpenAL 1.0");
		}
		
		this.soundListener = new SoundListener();
		this.soundBuffers = new HashMap<>();
		this.soundSources = new ArrayList<>();
	}
	
	public SoundListener getListener()
	{
		return this.soundListener;
	}
	
	public int addSoundfile(String fileName)
	{
		SoundBuffer savedSound = this.soundBuffers.get(fileName);
		if(savedSound == null)
		{
			SoundBuffer soundBuffer = new SoundBuffer(fileName);
			this.soundBuffers.put(fileName, soundBuffer);
			return soundBuffer.getBufferId();
		}
		else
		{
			return savedSound.getBufferId();
		}
	}
	
	public int addSource(boolean loop, boolean relative)
	{
		SoundSource soundSource = new SoundSource(false, false);
		this.soundSources.add(soundSource);
		return this.soundSources.size() - 1;
	}
	
	public SoundSource getSource(int sourceId)
	{
		return this.soundSources.get(sourceId);
	}
	
	public void setSoundToSource(int sound, int source)
	{
		this.soundSources.get(source).setBuffer(sound);
	}
	
	public void cleanUp()
	{
		for(SoundBuffer soundBuffer : this.soundBuffers.values())
		{
			soundBuffer.cleanUp();
		}
		for(SoundSource soundSource : this.soundSources)
		{
			soundSource.cleanUp();
		}

		alcDestroyContext(this.context);
		alcCloseDevice(this.device);
	}
	
	public void play(int source)
	{
		this.getSource(source).play();
	}
	
	public void playMusic()
	{
		//set max buffer size
		final int STREAM_BUFFER_SIZE = 1024;
		//create music sound source
		SoundSource musicSource = new SoundSource(false, false);
		//initiate array of buffer handles and fill the buffers
		int[] streamBuffers = new int[3];
		for(int i = 0; i < streamBuffers.length; i++)
		{
			streamBuffers[i] = alGenBuffers();
			alBufferData(streamBuffers[i], AL_FORMAT_MONO16, getStreamData(), 44100);
			alSourceQueueBuffers(musicSource.getSourceId(), streamBuffers[i]);
		}
		
	}
	
	public ByteBuffer getStreamData()
	{
		return null;
	}

}
