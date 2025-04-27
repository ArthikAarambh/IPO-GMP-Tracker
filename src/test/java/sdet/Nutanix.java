package sdet;


public class Nutanix {
    public static void main(String[] args) {
//        String str="toyota";
//        HashMap<Character,Integer> freq = new HashMap<>();
//        for(int i=0;i<str.length();i++){
//            if(!freq.containsKey(str.charAt(i))){
//                int value =1;
//                freq.put(str.charAt(i), value);
//            }else{
//                int val=freq.get(str.charAt(i));
//                val++;
//                freq.put(str.charAt(i),val);
//            }
//        }
//        for(int i=0;i<str.length();i++){
//            if(freq.get(str.charAt(i))==1){
//                System.out.println(str.charAt(i)+" "+freq.get(str.charAt(i)));
//            }
//
//        }

//        Str =aaaeeeeccddbba
//        Output:
//        a3e4c2d2b2a1
//        Longest Sequence is “e”

        String str = "aaaeeeeccddbba";
        String ans = "";
        int count =0;
        for(int i=0;i<str.length()-1;i++){
            if(str.charAt(i)==str.charAt(i+1))
            {
                count=count+1;
                continue;
            }
            ans=ans+str.charAt(i);
            count++;
            ans=ans+count;
            if(str.charAt(i)!=str.charAt(i+1)){
                count =0;
                if(i==str.length()-2){
                    ans=ans+str.charAt(i+1);
                    int val=1;
                    ans=ans+val;
                }
            }

        }
        System.out.println(ans);
    }

}
