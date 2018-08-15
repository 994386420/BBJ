package com.bbk.Bean;

public interface OnAddressListioner {
	void onDelete(String addrid);
	void onDefaultAdressCheck(String addrid);
	void onItemCick(String addrid,String phone,String receiver,String tag,String street,String area);
}
