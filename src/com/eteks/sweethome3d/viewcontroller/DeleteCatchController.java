/**
 *  This file is part of the spp(Superpolo Platform).
 *  Copyright (C) by SanPolo Co.Ltd.
 *  All rights reserved.
 *
 *  See http://www.spolo.org/ for more information.
 *
 *  SanPolo Co.Ltd
 *  http://www.spolo.org/
 *  Any copyright issues, please contact: copr@spolo.org
 **/

package com.eteks.sweethome3d.viewcontroller;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import com.eteks.sweethome3d.model.Catch;
import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.UserPreferences;

public class DeleteCatchController implements Controller {

	public enum Property {
		YANGBANJIAN_CATCH, JIAJU_CATCH, ACCOUNT_CATCH, CAIZHI_CATCH
	}

	public enum DeleteOrNotDelete {
		DELETE, NOTDELETE
	}

	private final Catch glueCatch;
	private final UserPreferences preferences;
	private final ViewFactory viewFactory;
	private final PropertyChangeSupport propertyChangeSupport;
	private DialogView deleteCatchView;

	private DeleteOrNotDelete YBJDeleteState;
	private DeleteOrNotDelete JJDeleteState;
	private DeleteOrNotDelete ZHDeleteState;
	private DeleteOrNotDelete CZDeleteState;
	private DeleteOrNotDelete ZXMBDeleteState;

	public DeleteCatchController(final Home home, UserPreferences preferences,
			ViewFactory viewFactory, Catch gluecatch) {
		this.glueCatch = gluecatch;
		this.preferences = preferences;
		this.viewFactory = viewFactory;
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	/**
	 * Returns the view associated with this controller.
	 */
	public DialogView getView() {
		// Create view lazily only once it's needed
		if (this.deleteCatchView == null) {
			this.deleteCatchView = this.viewFactory.createDeleteCatchView(
					this.preferences, this);
		}
		return this.deleteCatchView;
	}

	/**
	 * Displays the view controlled by this controller.
	 */
	public void displayView(View parentView) {
		getView().displayView(parentView);
	}

	/**
	 * Adds the property change <code>listener</code> in parameter to this
	 * controller.
	 */
	public void addPropertyChangeListener(Property property,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(property.name(),
				listener);
	}

	/**
	 * Removes the property change <code>listener</code> in parameter from this
	 * controller.
	 */
	public void removePropertyChangeListener(Property property,
			PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(
				property.name(), listener);
	}

	public DeleteOrNotDelete getYBJDeleteState() {
		return YBJDeleteState;
	}

	public void setYBJDeleteState(DeleteOrNotDelete yBJDeleteState) {
		YBJDeleteState = yBJDeleteState;
	}

	public DeleteOrNotDelete getJJDeleteState() {
		return JJDeleteState;
	}

	public void setJJDeleteState(DeleteOrNotDelete jJDeleteState) {
		JJDeleteState = jJDeleteState;
	}

	public DeleteOrNotDelete getZHDeleteState() {
		return ZHDeleteState;
	}

	public void setZHDeleteState(DeleteOrNotDelete zHDeleteState) {
		ZHDeleteState = zHDeleteState;
	}

	public DeleteOrNotDelete getCZDeleteState() {
		return CZDeleteState;
	}

	public void setCZDeleteState(DeleteOrNotDelete cZDeleteState) {
		CZDeleteState = cZDeleteState;
	}

	public DeleteOrNotDelete getZXMBDeleteState() {
		return ZXMBDeleteState;
	}

	public void setZXMBDeleteState(DeleteOrNotDelete zXMBDeleteState) {
		ZXMBDeleteState = zXMBDeleteState;
	}

	public void modifyCatchStatu() {
		doModifyCatchStatu();
	}

	private void updateYBJCatch() {
		if (this.getYBJDeleteState() == DeleteOrNotDelete.DELETE) {
			this.glueCatch.deleteYBJCatch();
		}
	}

	private void updateJJCatch() {
		if (this.getJJDeleteState() == DeleteOrNotDelete.DELETE) {
			this.glueCatch.deleteJJCatch();
		}
	}

	private void updateZHCatch() {
		if (this.getZHDeleteState() == DeleteOrNotDelete.DELETE) {
			this.glueCatch.deleteZHCatch();
		}
	}

	private void updateCZCatch() {
		if (this.getCZDeleteState() == DeleteOrNotDelete.DELETE) {
			this.glueCatch.deleteCZCatch();
		}
	}

	private void updateZXMBCatch() {
		if (this.getZXMBDeleteState() == DeleteOrNotDelete.DELETE) {
			this.glueCatch.deleteZXMBCatch();
		}
	}

	private void doModifyCatchStatu() {
		updateYBJCatch();
		updateJJCatch();
		updateZHCatch();
		updateCZCatch();
		updateZXMBCatch();
	}

}
