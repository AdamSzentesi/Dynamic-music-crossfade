package music;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import static music.SuperUtils.say;
import org.lwjgl.BufferUtils;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.MemoryStack.stackPop;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class SoundBuffer
{
	private int bufferId;

	public SoundBuffer(String fileName)
	{
		this.bufferId = alGenBuffers();
		
		//Allocate space to store return information from the function
		stackPush();
		IntBuffer channelsBuffer = stackMallocInt(1);
		stackPush();
		IntBuffer sampleRateBuffer = stackMallocInt(1);
		
		ShortBuffer rawAudioBuffer = stb_vorbis_decode_filename("res/music/" + fileName, channelsBuffer, sampleRateBuffer);
		
		//Retreive the extra information that was stored in the buffers by the function
		int channels = channelsBuffer.get();
		int sampleRate = sampleRateBuffer.get();
		//Free the space we allocated earlier
		stackPop();
		stackPop();
		
		//Find the correct OpenAL format
		int format = -1;
		if(channels == 1)
		{
			format = AL_FORMAT_MONO16;
		}
		else if(channels == 2)
		{
			format = AL_FORMAT_STEREO16;
		}

		//Send the data to OpenAL
		alBufferData(this.bufferId, format, rawAudioBuffer, sampleRate);

		//Free the memory allocated by STB
		free(rawAudioBuffer);
	}
	
	public int getBufferId()
	{
		return this.bufferId;
  }

  public void cleanUp()
	{
		alDeleteBuffers(this.bufferId);
  }
	
	public void setBufferPart(String fileName)
	{

	}
	
//	static ShortBuffer readVorbis(String resource, int bufferSize, STBVorbisInfo info)
//	{
//		ByteBuffer vorbis;
//		try
//		{
//				vorbis = ioResourceToByteBuffer(resource, bufferSize);
//		}
//		catch (IOException e)
//		{
//				throw new RuntimeException(e);
//		}
//
//		IntBuffer error   = BufferUtils.createIntBuffer(1);
//		Long decoder = stb_vorbis_open_memory(vorbis, error, null);
//		if (decoder == null)
//		{
//				throw new RuntimeException("Failed to open Ogg Vorbis file. Error: " + error.get(0));
//		}
//
//		stb_vorbis_get_info(decoder, info);
//
//		int channels = info.channels();
//
//		int lengthSamples = stb_vorbis_stream_length_in_samples(decoder);
//
//		ShortBuffer pcm = BufferUtils.createShortBuffer(lengthSamples);
//
//		pcm.limit(stb_vorbis_get_samples_short_interleaved(decoder, channels, pcm) * channels);
//		stb_vorbis_close(decoder);
//
//		return pcm;
//  }
	
}
