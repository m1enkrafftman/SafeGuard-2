package io.github.m1enkrafftman.safeguard2.checks.movement;

import io.github.m1enkrafftman.safeguard2.checks.SGCheck;
import io.github.m1enkrafftman.safeguard2.checks.SGCheckTag;
import io.github.m1enkrafftman.safeguard2.utils.player.PlayerThread;

public class SGCheckWaterwalk extends SGCheck {
	
	@Override
	public void check(float millisDif, SGCheckTag checkTag, PlayerThread thread) {

		//if(foot-0.1 isLiquid or legs isLiquid or torso isLiquid
			//if deltaHorizontal > currentSpeed(walk/sprint/sneak etc)*liquidMulti(magic number gathered from tests
				//publish = true
			//if deltaVertical > verticalMoveCap(magic number gathered from testing)
				//publish = true
			//else
				//nothing, we don't care
		//publish and stuff
		
	}

}
