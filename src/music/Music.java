package music;

import java.util.logging.Level;
import java.util.logging.Logger;
import static music.SuperUtils.say;

public class Music
{
	public static void main(String[] args)
	{
		SoundManager soundManager = new SoundManager();
		soundManager.getListener().setPosition(new Vector3f(0, 0, 0));
		soundManager.getListener().setVelocity(new Vector3f(0, 0, 0));
		soundManager.getListener().setOrientation(new Vector3f(0, 0, -1), new Vector3f(0, 1, 0));
		
		SoundStream soundStream = new SoundStream("storm_fight_mono.ogg");
		StreamSource streamSource = new StreamSource(false, false);
		streamSource.setStream(soundStream);
		streamSource.setPosition(new Vector3f(0, 0, 0));
		streamSource.setVelocity(new Vector3f(0, 0, 0));
		
		SoundStream soundStream2 = new SoundStream("storm_calm_mono.ogg");
		StreamSource streamSource2 = new StreamSource(false, false);
		streamSource2.setStream(soundStream2);
		streamSource2.setPosition(new Vector3f(0, 0, 0));
		streamSource2.setVelocity(new Vector3f(0, 0, 0));
		
//		int sound = soundManager.addSoundfile("storm_calm_mono.ogg");
//		int source = soundManager.addSource(false, false);
//		soundManager.getSource(source).setBuffer(sound);
//		soundManager.getSource(source).setPosition(new Vector3f(-2, 0, 0));
//		soundManager.getSource(source).setVelocity(new Vector3f(0, 0, 0));
//		
//		int sound2 = soundManager.addSoundfile("storm_fight_mono.ogg");
//		int source2 = soundManager.addSource(false, false);
//		soundManager.getSource(source2).setBuffer(sound2);
//		soundManager.getSource(source2).setPosition(new Vector3f(2, 0, 0));
//		
//		soundManager.play(source);
//		soundManager.play(source2);

		streamSource.play();
		streamSource2.play();

		float time = -90f;
		while(streamSource.isPlaying() || streamSource2.isPlaying())
		{
			float gain = (float)Math.sin(Math.toRadians(time)) / 2 + 0.5f;
			float gain2 = (float)Math.sin(Math.toRadians(time + 180)) / 2 + 0.5f;
//			say("" + gain);
			
			streamSource.setGain(gain);
			streamSource2.setGain(gain2);
			rest(50);
			time = time + 0.9f;
		}
	
		soundStream.cleanUp();
		soundStream2.cleanUp();
		soundManager.cleanUp();
	}
	
	private static void rest(long millis)
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

	
}
