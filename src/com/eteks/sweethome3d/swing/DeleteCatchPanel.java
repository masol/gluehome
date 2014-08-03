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

package com.eteks.sweethome3d.swing;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.eteks.sweethome3d.model.UserPreferences;
import com.eteks.sweethome3d.viewcontroller.DeleteCatchController;
import com.eteks.sweethome3d.viewcontroller.DialogView;
import com.eteks.sweethome3d.viewcontroller.View;

public class DeleteCatchPanel extends JPanel implements DialogView {

	private static final long serialVersionUID = 1L;
	private UserPreferences preferences;
	private DeleteCatchController controller;
	private String dialogTitle;
	private JComponent nameTextField;
	private JCheckBox deleteYBJCheckBox;
	private JCheckBox deleteJJCheckBox;
	private JCheckBox deleteZHCheckBox;
	private JCheckBox deleteCZCheckBox;
	private JCheckBox deleteZXMBCheckBox;

	/**
	 * Displays this panel in a modal dialog box.
	 */
	public void displayView(View parentView) {
		if (SwingTools.showConfirmDialog((JComponent) parentView, this,
				this.dialogTitle, this.nameTextField) == JOptionPane.OK_OPTION
				&& this.controller != null) {
			this.controller.modifyCatchStatu();
			updataCatchState();
		} else {
			updataCatchState();
		}
	}

	public DeleteCatchPanel(UserPreferences preferences,
			DeleteCatchController controller) {
		super(new GridBagLayout());
		this.setLayout(new GridBagLayout());
		this.preferences = preferences;
		this.controller = controller;
		this.dialogTitle = SwingTools.getLocalizedLabelText(preferences,
				DeleteCatchPanel.class, "title");
		createComponents(this.preferences, this.controller);
		layoutComponents();

	}

	public void updataCatchState() {
		if (this.deleteCZCheckBox.isSelected()) {
			this.deleteCZCheckBox.doClick();
		}
		if (this.deleteJJCheckBox.isSelected()) {
			this.deleteJJCheckBox.doClick();
		}
		if (this.deleteYBJCheckBox.isSelected()) {
			this.deleteYBJCheckBox.doClick();
		}
		if (this.deleteZHCheckBox.isSelected()) {
			this.deleteZHCheckBox.doClick();
		}
		if (this.deleteZXMBCheckBox.isSelected()) {
			this.deleteZXMBCheckBox.doClick();
		}
	}

	/*
	 * 创建界面组件对象
	 */
	private void createComponents(UserPreferences preferences,
			final DeleteCatchController controller) {
		this.deleteJJCheckBox = new JCheckBox(SwingTools.getLocalizedLabelText(
				preferences, DeleteCatchPanel.class, "deleteJJCheckBox.Name"));
		this.deleteJJCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (deleteJJCheckBox.isSelected()) {
					controller
							.setJJDeleteState(DeleteCatchController.DeleteOrNotDelete.DELETE);
				} else {
					System.out.println("notdeleteJJCheckBox");
					controller
							.setJJDeleteState(DeleteCatchController.DeleteOrNotDelete.NOTDELETE);
				}
			}
		});
		this.deleteYBJCheckBox = new JCheckBox(
				SwingTools.getLocalizedLabelText(preferences,
						DeleteCatchPanel.class, "deleteYBJCheckBox.Name"));
		this.deleteYBJCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (deleteYBJCheckBox.isSelected()) {
					controller
							.setYBJDeleteState(DeleteCatchController.DeleteOrNotDelete.DELETE);
				} else {
					controller
							.setYBJDeleteState(DeleteCatchController.DeleteOrNotDelete.NOTDELETE);
				}
			}
		});
		this.deleteZHCheckBox = new JCheckBox(SwingTools.getLocalizedLabelText(
				preferences, DeleteCatchPanel.class, "deleteZHCheckBox.Name"));
		this.deleteZHCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (deleteZHCheckBox.isSelected()) {
					controller
							.setZHDeleteState(DeleteCatchController.DeleteOrNotDelete.DELETE);
				} else {
					controller
							.setZHDeleteState(DeleteCatchController.DeleteOrNotDelete.NOTDELETE);
				}
			}
		});

		this.deleteCZCheckBox = new JCheckBox(SwingTools.getLocalizedLabelText(
				preferences, DeleteCatchPanel.class, "deleteCZCheckBox.Name"));
		this.deleteCZCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (deleteCZCheckBox.isSelected()) {
					controller
							.setCZDeleteState(DeleteCatchController.DeleteOrNotDelete.DELETE);
				} else {
					controller
							.setCZDeleteState(DeleteCatchController.DeleteOrNotDelete.NOTDELETE);
				}
			}
		});

		this.deleteZXMBCheckBox = new JCheckBox(SwingTools.getLocalizedLabelText(
				preferences, DeleteCatchPanel.class, "deleteZXMBCheckBox.Name"));
		this.deleteZXMBCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (deleteZXMBCheckBox.isSelected()) {
					controller.setZXMBDeleteState(DeleteCatchController.DeleteOrNotDelete.DELETE);
				} else {
					controller
							.setZXMBDeleteState(DeleteCatchController.DeleteOrNotDelete.NOTDELETE);
				}
			}
		});
	}

	/*
	 * 给界面布局
	 */
	private void layoutComponents() {
		JPanel YBJPanel = SwingTools.createTitledPanel(SwingTools
				.getLocalizedLabelText(preferences, DeleteCatchPanel.class,
						"YBJPanel.Name"));
		JPanel JJPanel = SwingTools.createTitledPanel(SwingTools
				.getLocalizedLabelText(preferences, DeleteCatchPanel.class,
						"JJPanel.Name"));
		JPanel ZHPanel = SwingTools.createTitledPanel(SwingTools
				.getLocalizedLabelText(preferences, DeleteCatchPanel.class,
						"ZHPanel.Name"));
		JPanel ZXMBPanel = SwingTools.createTitledPanel(SwingTools
				.getLocalizedLabelText(preferences, DeleteCatchPanel.class,
						"ZXMBPanel.Name"));
		JPanel CZPanel = SwingTools.createTitledPanel(SwingTools
				.getLocalizedLabelText(preferences, DeleteCatchPanel.class,
						"CZPanel.Name"));

		YBJPanel.add(this.deleteYBJCheckBox, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

		JJPanel.add(this.deleteJJCheckBox, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

		ZHPanel.add(this.deleteZHCheckBox, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

		CZPanel.add(this.deleteCZCheckBox, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));
		
		ZXMBPanel.add(this.deleteZXMBCheckBox, new GridBagConstraints(0, 0, 1, 1,
				1, 0, GridBagConstraints.LINE_START,
				GridBagConstraints.VERTICAL, new Insets(0, 0, 0, 0), 0, 0));

		add(YBJPanel, new GridBagConstraints(1, 0, 1, 1, 1, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
		
		add(ZXMBPanel, new GridBagConstraints(1, 1, 1, 1, 1, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(JJPanel, new GridBagConstraints(1, 2, 1, 1, 1, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(CZPanel, new GridBagConstraints(1, 3, 1, 1, 1, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(ZHPanel, new GridBagConstraints(1, 4, 1, 1, 1, 0,
				GridBagConstraints.LINE_START, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));
	}

}
