package cn.xyz.common.config;

public interface Op {
    String EQ = " = ";
    String NE = " != ";
    String GT = " > ";
    String LT = " < ";
    String GE = " >= ";
    String LE = " <= ";
    String LPAREN = "(";
    String RPAREN = ")";
    String PCT = "%";//percent
    String LIKE = " like ";
    String NLIKE = " not like ";
    String IN = " in ";
    String NIN = " not in ";
    String IS_E = " = '' ";
    String ISN_E = " != '' ";
    String IS_N = " is null ";
    String ISN_N = " is not null ";
    String IS_EMPTY = "isEmpty";
    String IS_NOT_EMPTY = "isNotEmpty";
    String DATE = "date";
    String SN = "sn";
}
