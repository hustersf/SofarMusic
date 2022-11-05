package com.sf.daogenerator;

import java.io.File;
import java.util.HashMap;

import com.sf.daogenerator.io.FileHelper;

/**
 * AndroidX 替换 import 脚本
 */
public class AndroidXScript {

  public static void main(String args[]) throws Exception {
    String srcDir = "/Users/sufan/个人/Android/SofarMusic";
    String destDir = "/Users/sufan/个人/Android2/SofarMusic";

    FileHelper helper = new FileHelper(new File(srcDir), new File(destDir));
    helper.addImportKeyValue("import android.support.annotation.NonNull;",
      "import androidx.annotation.NonNull;");
    helper.addImportKeyValue("import android.support.annotation.Nullable;",
      "import androidx.annotation.Nullable;");
    helper.addImportKeyValue("import android.support.annotation.IntDef;",
      "import androidx.annotation.IntDef;");
    helper.addImportKeyValue("import android.support.annotation.StringRes;",
      "import androidx.annotation.StringRes;");

    helper.addImportKeyValue("import android.support.v4.app.Fragment;",
      "import androidx.fragment.app.Fragment;");
    helper.addImportKeyValue("import android.support.v4.app.FragmentManager;",
      "import androidx.fragment.app.FragmentManager;");
    helper.addImportKeyValue("import android.support.v4.app.FragmentPagerAdapter;",
      "import androidx.fragment.app.FragmentPagerAdapter;");

    helper.addImportKeyValue("import android.support.v4.view.ViewPager;",
      "import androidx.viewpager.widget.ViewPager;");
    helper.addImportKeyValue("import android.support.v4.view.PagerAdapter;",
      "import androidx.viewpager.widget.PagerAdapter;");

    helper.addImportKeyValue("import android.support.v7.widget.RecyclerView;",
      "import androidx.recyclerview.widget.RecyclerView;");
    helper.addImportKeyValue("import android.support.v7.widget.LinearLayoutManager;",
      "import androidx.recyclerview.widget.LinearLayoutManager;");
    helper.addImportKeyValue("import android.support.v7.widget.GridLayoutManager;",
      "import androidx.recyclerview.widget.GridLayoutManager;");
    helper.addImportKeyValue("import android.support.v7.widget.StaggeredGridLayoutManager;",
      "import androidx.recyclerview.widget.StaggeredGridLayoutManager;");

    helper.addImportKeyValue("import android.support.v7.widget.AppCompatTextView;",
      "import androidx.appcompat.widget.AppCompatTextView;");
    helper.addImportKeyValue("import android.support.v7.widget.AppCompatImageView;",
      "import androidx.appcompat.widget.AppCompatImageView;");

    System.out.println("----------AndroidXScript running--------");
    helper.start();
    System.out.println("----------AndroidXScript success--------");
  }
}
