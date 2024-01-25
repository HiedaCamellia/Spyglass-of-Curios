package cn.solarmoon.spyglass_of_curios.util;

import cn.solarmoon.spyglass_of_curios.common.ic.ISpyUser;
import cn.solarmoon.spyglass_of_curios.init.Config;
import net.minecraft.util.Mth;

import static cn.solarmoon.spyglass_of_curios.util.CUtil.deBug;

public class FovAlgorithm {

    public static double delta = 1.5;
    public static double setFov = 10;
    public static int minFov = Config.minMultiplier.get();
    public static int maxFov = Config.maxMultiplier.get();

    private final ISpyUser sp;
    public FovAlgorithm(ISpyUser sp) {
        this.sp = sp;
    }

    //滚轮调焦
    public void setFov(double tagDouble, double scroll) {
        double sum = tagDouble + scroll;
        setFov = Mth.clamp(sum, minFov, maxFov);
        deBug("min:"+minFov+", "+"max:"+maxFov);
        sp.setMultiplier(setFov>0 ? Mt.alg(setFov) : 1-setFov/10);
        deBug("MULTIPLIER:" + sp.multiplier());
    }

    // 设置默认为tag值
    public void setDefaultFov(double tagDouble) {
        sp.setMultiplier(tagDouble>0 ? Mt.alg(tagDouble) : 1-tagDouble/10);
        deBug("TagMUL: " + sp.multiplier());
    }

    // 设置默认值
    public void resetFov() {
        sp.setMultiplier(Mt.alg(Config.defaultMultiplier.get()));
        deBug("resetMUL: " + sp.multiplier());
    }

    // 设置tag值
    public double putTag() {
        double set = sp.multiplier() > 0 && sp.multiplier() <= 1 ? Math.round(Mt.argAlg(sp.multiplier())) : - (sp.multiplier() * 10-10);
        deBug("putTag: " + set);
        return set;
    }

    public static class Mt {
        public static double alg(double n) {
            if((int )n == 0) return 1.0;
            return 1 / Math.pow(delta, n);
        }
        public static double argAlg(double i) {
            return Math.log(1 / i) / Math.log(delta);
        }
    }


}
