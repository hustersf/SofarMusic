package com.sf.sofarmusic.demo.list.sort;

import com.sf.sofarmusic.demo.enity.BankItem;

import java.util.Comparator;

public class PinyinComparator implements Comparator<BankItem> {

	@Override
	public int compare(BankItem arg0, BankItem arg1) {
		// TODO Auto-generated method stub
		if (arg0.letter.equals("@") || arg1.letter.equals("#")) {
			return -1;     
		} else if (arg0.letter.equals("#") || arg1.letter.equals("@")) {
			return 1;
		} else {
			return arg0.letter.compareTo(arg1.letter); // 升序
		//    return arg1.getLetter().compareTo(arg0.getLetter()); // 降序
		}
	}
}
