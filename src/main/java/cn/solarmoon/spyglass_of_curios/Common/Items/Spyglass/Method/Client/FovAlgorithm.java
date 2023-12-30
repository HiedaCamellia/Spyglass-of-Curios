package cn.solarmoon.spyglass_of_curios.Common.Items.Spyglass.Method.Client;

import cn.solarmoon.spyglass_of_curios.Init.Config;
import net.minecraft.util.Mth;

import static cn.solarmoon.spyglass_of_curios.Util.Constants.MULTIPLIER;
import static cn.solarmoon.spyglass_of_curios.Util.DeBug.deBug;

public class FovAlgorithm {

    public static double delta = 1.5;
    public static double setFov = 10;
    public static int minFov = Config.minMultiplier.get();
    public static int maxFov = Config.maxMultiplier.get();

    //滚轮调焦
    public static void setFov(double tagDouble, double scroll) {
        double sum = tagDouble + scroll;
        setFov = Mth.clamp(sum, minFov, maxFov);
        deBug("min:"+minFov+", "+"max:"+maxFov);
        MULTIPLIER = setFov>0 ? new Mt().alg(setFov) : 1-setFov/10;
        deBug("MULTIPLIER:" + MULTIPLIER);
    }

    // 设置默认为tag值
    public static void setDefaultFov(double tagDouble) {
        MULTIPLIER = tagDouble>0 ? new Mt().alg(tagDouble) : 1-tagDouble/10;
        deBug("TagMUL: " + MULTIPLIER);
    }

    // 设置默认值
    public static void resetFov() {
        MULTIPLIER = new Mt().alg(Config.defaultMultiplier.get());
        deBug("resetMUL: " + MULTIPLIER);
    }

    // 设置tag值
    public static double putTag() {
        double set = MULTIPLIER > 0 && MULTIPLIER <= 1 ? Math.round(new Mt().argAlg(MULTIPLIER)) : - (MULTIPLIER * 10-10);
        deBug("putTag: " + set);
        return set;
    }

    public static class Mt {
        public double alg(double n) {
            if((int )n == 0) return 1.0;
            return 1 / Math.pow(delta, n);
        }
        public double argAlg(double i) {
            return Math.log(1 / i) / Math.log(delta);
        }
    }


}
