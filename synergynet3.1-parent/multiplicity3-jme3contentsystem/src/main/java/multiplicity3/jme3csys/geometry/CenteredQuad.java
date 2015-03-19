package multiplicity3.jme3csys.geometry;

/*
 * Copyright (c) 2009-2010 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import java.nio.FloatBuffer;

import com.jme3.scene.*;
import com.jme3.scene.VertexBuffer.Type;

public class CenteredQuad extends Mesh {

	private float width;
	private float height;
	private boolean flipCoords;

	/**
	 * Do not use this constructor. Serialization purposes only.
	 */
	public CenteredQuad(){
	}

	public CenteredQuad(float width, float height){
		this(width, height, false);
	}

	public CenteredQuad(float width, float height, boolean flipCoords){		
		this.width = width;
		this.height = height;
		this.flipCoords = flipCoords;
		initBuffers();
	}

	public float getHeight() {
		return height;
	}

	public float getWidth() {
		return width;
	}

	private void initBuffers() {
		setBuffer(Type.Position, 3, new float[]{-width/2,      -height/2,      0,
				width/2,  -height/2,      0,
				width/2,  height/2, 0,
				-width/2,      height/2, 0
		});


		if (flipCoords){
			setBuffer(Type.TexCoord, 2, new float[]{0, 1,
					1, 1,
					1, 0,
					0, 0});
		}else{
			setBuffer(Type.TexCoord, 2, new float[]{0, 0,
					1, 0,
					1, 1,
					0, 1});
		}
		setBuffer(Type.Normal, 3, new float[]{0, 0, 1,
				0, 0, 1,
				0, 0, 1,
				0, 0, 1});
		if (height < 0){
			setBuffer(Type.Index, 3, new short[]{0, 2, 1,
					0, 3, 2});
		}else{
			setBuffer(Type.Index, 3, new short[]{0, 1, 2,
					0, 2, 3});
		}
		setDynamic();
		updateBound();
	}

	public void updateGeometry(float width, float height) {
		this.width = width;
		this.height = height;
		FloatBuffer fb = (FloatBuffer) getBuffer(Type.Position).getData();
		fb.rewind();
		fb.put(-width/2).put(-height/2).put(0);
		fb.put(width/2).put(-height/2).put(0);
		fb.put(width/2).put(height/2).put(0);
		fb.put(-width/2).put(height/2).put(0);
		getBuffer(Type.Position).updateData(fb);
		updateBound();
	}


}
