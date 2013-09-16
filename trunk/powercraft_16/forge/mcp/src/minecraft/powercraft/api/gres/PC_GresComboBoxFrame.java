package powercraft.api.gres;

class PC_GresComboBoxFrame extends PC_GresFrame {

	private PC_GresComboBox comboBox;
	
	protected PC_GresComboBoxFrame(PC_GresComboBox comboBox){
		this.comboBox = comboBox;
	}
	
	@Override
	protected void setParent(PC_GresContainer parent) {
		if(parent instanceof PC_GresGuiHandler)
			super.setParent(parent);
	}

	@Override
	public void putInRect(int x, int y, int width, int height) {
		setLocation(comboBox.getRealLocation().add(0, comboBox.rect.height));
	}
	
}
