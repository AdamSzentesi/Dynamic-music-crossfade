package music;

import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.openal.AL10.AL_BUFFERS_PROCESSED;
import static org.lwjgl.openal.AL10.alGetSourcei;
import static org.lwjgl.openal.AL10.alSourceQueueBuffers;
import static org.lwjgl.openal.AL10.alSourceUnqueueBuffers;

public class MusicPlayer implements Runnable
{
	private Thread thread;
	private String threadName;
	private StreamSource streamSource;
	
	public MusicPlayer(String threadName)
	{
		this.threadName = threadName;
		System.out.println("thread '" + threadName + "' created");
	}
	
	public void run()
	{
		while(streamSource.isPlaying())
		{
			int sourceId = streamSource.getSourceId();
			//get number of processed buffers
			int processedBuffers = alGetSourcei(sourceId, AL_BUFFERS_PROCESSED);
			//fill the processed buffer with new data
			for(int i = 0; i < processedBuffers; i++)
			{
				int removedBuffer = alSourceUnqueueBuffers(sourceId);
				//if possible to fill up the removed buffer
				if(streamSource.getSoundStream().stream(removedBuffer))
				{
					//put it back to queue
					alSourceQueueBuffers(sourceId, removedBuffer);
				}
			}
			rest(50);
		}
	}
	
	private void rest(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException ex)
		{
			Logger.getLogger(SoundStream.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void start(StreamSource streamSource)
	{
		this.streamSource = streamSource;
		this.thread = new Thread(this, this.threadName);
		this.thread.setDaemon(true);
		this.thread.start();
		System.out.println("thread '" + threadName + "' started");
	}
}