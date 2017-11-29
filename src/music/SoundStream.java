package music;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import static music.SuperUtils.say;
import org.lwjgl.BufferUtils;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import org.lwjgl.stb.STBVorbisInfo;


public class SoundStream
{
	private final int NUM_BUFFERS = 3;
	private int[] streamBuffers;
	private final int STREAM_BUFFER_SIZE = 1024 * 4;
	
	private int channels;
	private int sampleRate;
	private int format;
	private float seconds;
	private int samples;
	
	private long decoder;
	private ShortBuffer pcm;
	
	public SoundStream(String fileName)
	{
		//open file from drive
		IntBuffer error   = BufferUtils.createIntBuffer(1);
		decoder = stb_vorbis_open_filename("res/music/" + fileName, error, null);
		say("decoder: " + decoder);
		
		//get file info
		STBVorbisInfo info = STBVorbisInfo.malloc();
		stb_vorbis_get_info(decoder, info);
		this.channels = info.channels();
		this.sampleRate = info.sample_rate();
		this.format = getFormat(channels);
		this.samples = stb_vorbis_stream_length_in_samples(decoder);
    this.seconds = stb_vorbis_stream_length_in_seconds(decoder);
		say("chanels: " + this.channels);
		say("sample rate: " + this.sampleRate);
		say("format: " + this.format);
		say("samples: " + this.samples);
		say("seconds: " + this.seconds);
		
		//get max samples per channel
		this.pcm = BufferUtils.createShortBuffer(STREAM_BUFFER_SIZE);
		
		//initiate array of buffer handles and fill the buffers
		this.streamBuffers = new int[NUM_BUFFERS];
		for(int i = 0; i < this.streamBuffers.length; i++)
		{
			this.streamBuffers[i] = alGenBuffers();
			stream(this.streamBuffers[i]);
		}
	}
	
	public boolean stream(int buffer)
	{
		//get max samples per channel
		int samplesPerChannel = stb_vorbis_get_samples_short_interleaved(this.decoder, this.channels, this.pcm);
		if(samplesPerChannel == 0)
		{
			return false;
		}
		
		//mount data
		pcm.position(0);
		alBufferData(buffer, this.format, this.pcm, this.sampleRate);
		
		return true;
	}
	
	private int getFormat(int channels)
	{
		switch(channels)
		{
			case 1:
				return AL_FORMAT_MONO16;
			case 2:
				return AL_FORMAT_STEREO16;
			default:
				return 0;
		}
	}

	public int[] getStreamBuffers()
	{
		return this.streamBuffers;
  }

  public void cleanUp()
	{
		for(int i = 0; i < this.streamBuffers.length; i++)
		{
			alDeleteBuffers(this.streamBuffers[i]);
		}
  }
	
}
