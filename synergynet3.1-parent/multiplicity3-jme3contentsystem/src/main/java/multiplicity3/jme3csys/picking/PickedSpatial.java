/*
 * Copyright (c) 2008 University of Durham, England All rights reserved.
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: *
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. * Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of 'SynergySpace' nor the names of
 * its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission. THIS SOFTWARE IS PROVIDED
 * BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package multiplicity3.jme3csys.picking;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * The Class PickedSpatial.
 */
public class PickedSpatial
{

	/** The point of selection. */
	private Vector3f pointOfSelection;

	/** The spatial. */
	private Spatial spatial;

	/**
	 * Instantiates a new picked spatial.
	 *
	 * @param s
	 *            the s
	 * @param pointOfSelection
	 *            the point of selection
	 */
	public PickedSpatial(Spatial s, Vector3f pointOfSelection)
	{
		this.spatial = s;
		this.pointOfSelection = pointOfSelection;
	}

	/**
	 * Gets the point of selection.
	 *
	 * @return the point of selection
	 */
	public Vector3f getPointOfSelection()
	{
		return pointOfSelection;
	}

	/**
	 * Gets the spatial.
	 *
	 * @return the spatial
	 */
	public Spatial getSpatial()
	{
		return spatial;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return PickedSpatial.class.getName() + " picked " + spatial.getName() + " at " + pointOfSelection;
	}
}
