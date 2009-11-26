/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class NVTextureShader {
	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 * and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * and GetDoublev, and by the &lt;target&gt; parameter of TexEnvf, TexEnvfv,
	 * TexEnvi, TexEnviv, GetTexEnvfv, and GetTexEnviv.
	 */
	public static final int GL_TEXTURE_SHADER_NV = 0x86de;
	/**
	 * When the &lt;target&gt; parameter of TexEnvf, TexEnvfv, TexEnvi, TexEnviv,
	 * GetTexEnvfv, and GetTexEnviv is TEXTURE_SHADER_NV, then the value
	 * of &lt;pname&gt; may be:
	 */
	public static final int GL_RGBA_UNSIGNED_DOT_PRODUCT_MAPPING_NV = 0x86d9;
	public static final int GL_SHADER_OPERATION_NV = 0x86df;
	public static final int GL_OFFSET_TEXTURE_SCALE_NV = 0x86e2;
	public static final int GL_OFFSET_TEXTURE_BIAS_NV = 0x86e3;
	public static final int GL_OFFSET_TEXTURE_2D_SCALE_NV = 0x86e2;
	public static final int GL_OFFSET_TEXTURE_2D_BIAS_NV = 0x86e3;
	public static final int GL_PREVIOUS_TEXTURE_INPUT_NV = 0x86e4;
	/**
	 * When the &lt;target&gt; parameter of TexEnvfv, TexEnviv, GetTexEnvfv, and
	 * GetTexEnviv is TEXTURE_SHADER_NV, then the value of &lt;pname&gt; may be:
	 */
	public static final int GL_CULL_MODES_NV = 0x86e0;
	public static final int GL_OFFSET_TEXTURE_MATRIX_NV = 0x86e1;
	public static final int GL_OFFSET_TEXTURE_2D_MATRIX_NV = 0x86e1;
	public static final int GL_CONST_EYE_NV = 0x86e5;
	/**
	 * When the &lt;target&gt; parameter GetTexEnvfv and GetTexEnviv is
	 * TEXTURE_SHADER_NV, then the value of &lt;pname&gt; may be:
	 */
	public static final int GL_SHADER_CONSISTENT_NV = 0x86dd;
	/**
	 * When the &lt;target&gt; and &lt;pname&gt; parameters of TexEnvf, TexEnvfv,
	 * TexEnvi, and TexEnviv are TEXTURE_SHADER_NV and SHADER_OPERATION_NV
	 * respectively, then the value of &lt;param&gt; or the value pointed to by
	 * &lt;params&gt; may be:
	 */
	public static final int GL_PASS_THROUGH_NV = 0x86e6;
	public static final int GL_CULL_FRAGMENT_NV = 0x86e7;
	public static final int GL_OFFSET_TEXTURE_2D_NV = 0x86e8;
	public static final int GL_OFFSET_TEXTURE_RECTANGLE_NV = 0x864c;
	public static final int GL_OFFSET_TEXTURE_RECTANGLE_SCALE_NV = 0x864d;
	public static final int GL_DEPENDENT_AR_TEXTURE_2D_NV = 0x86e9;
	public static final int GL_DEPENDENT_GB_TEXTURE_2D_NV = 0x86ea;
	public static final int GL_DOT_PRODUCT_NV = 0x86ec;
	public static final int GL_DOT_PRODUCT_DEPTH_REPLACE_NV = 0x86ed;
	public static final int GL_DOT_PRODUCT_TEXTURE_2D_NV = 0x86ee;
	public static final int GL_DOT_PRODUCT_TEXTURE_RECTANGLE_NV = 0x864e;
	public static final int GL_DOT_PRODUCT_TEXTURE_CUBE_MAP_NV = 0x86f0;
	public static final int GL_DOT_PRODUCT_DIFFUSE_CUBE_MAP_NV = 0x86f1;
	public static final int GL_DOT_PRODUCT_REFLECT_CUBE_MAP_NV = 0x86f2;
	public static final int GL_DOT_PRODUCT_CONST_EYE_REFLECT_CUBE_MAP_NV = 0x86f3;
	/**
	 * Accepted by the &lt;format&gt; parameter of GetTexImage, TexImage1D,
	 * TexImage2D, TexSubImage1D, and TexSubImage2D.
	 */
	public static final int GL_HILO_NV = 0x86f4;
	public static final int GL_DSDT_NV = 0x86f5;
	public static final int GL_DSDT_MAG_NV = 0x86f6;
	public static final int GL_DSDT_MAG_VIB_NV = 0x86f7;
	/**
	 * Accepted by the &lt;type&gt; parameter of GetTexImage, TexImage1D,
	 * TexImage2D, TexSubImage1D, and TexSubImage2D.
	 */
	public static final int GL_UNSIGNED_INT_S8_S8_8_8_NV = 0x86da;
	public static final int GL_UNSIGNED_INT_8_8_S8_S8_REV_NV = 0x86db;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of CopyTexImage1D,
	 * CopyTexImage2D, TexImage1D, and TexImage2D.
	 */
	public static final int GL_SIGNED_RGBA_NV = 0x86fb;
	public static final int GL_SIGNED_RGBA8_NV = 0x86fc;
	public static final int GL_SIGNED_RGB_NV = 0x86fe;
	public static final int GL_SIGNED_RGB8_NV = 0x86ff;
	public static final int GL_SIGNED_LUMINANCE_NV = 0x8701;
	public static final int GL_SIGNED_LUMINANCE8_NV = 0x8702;
	public static final int GL_SIGNED_LUMINANCE_ALPHA_NV = 0x8703;
	public static final int GL_SIGNED_LUMINANCE8_ALPHA8_NV = 0x8704;
	public static final int GL_SIGNED_ALPHA_NV = 0x8705;
	public static final int GL_SIGNED_ALPHA8_NV = 0x8706;
	public static final int GL_SIGNED_INTENSITY_NV = 0x8707;
	public static final int GL_SIGNED_INTENSITY8_NV = 0x8708;
	public static final int GL_SIGNED_RGB_UNSIGNED_ALPHA_NV = 0x870c;
	public static final int GL_SIGNED_RGB8_UNSIGNED_ALPHA8_NV = 0x870d;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D and
	 * TexImage2D.
	 */
	public static final int GL_HILO16_NV = 0x86f8;
	public static final int GL_SIGNED_HILO_NV = 0x86f9;
	public static final int GL_SIGNED_HILO16_NV = 0x86fa;
	public static final int GL_DSDT8_NV = 0x8709;
	public static final int GL_DSDT8_MAG8_NV = 0x870a;
	public static final int GL_DSDT_MAG_INTENSITY_NV = 0x86dc;
	public static final int GL_DSDT8_MAG8_INTENSITY8_NV = 0x870b;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, GetDoublev, PixelTransferf, and PixelTransferi.
	 */
	public static final int GL_HI_SCALE_NV = 0x870e;
	public static final int GL_LO_SCALE_NV = 0x870f;
	public static final int GL_DS_SCALE_NV = 0x8710;
	public static final int GL_DT_SCALE_NV = 0x8711;
	public static final int GL_MAGNITUDE_SCALE_NV = 0x8712;
	public static final int GL_VIBRANCE_SCALE_NV = 0x8713;
	public static final int GL_HI_BIAS_NV = 0x8714;
	public static final int GL_LO_BIAS_NV = 0x8715;
	public static final int GL_DS_BIAS_NV = 0x8716;
	public static final int GL_DT_BIAS_NV = 0x8717;
	public static final int GL_MAGNITUDE_BIAS_NV = 0x8718;
	public static final int GL_VIBRANCE_BIAS_NV = 0x8719;
	/**
	 * Accepted by the &lt;pname&gt; parameter of TexParameteriv, TexParameterfv,
	 * GetTexParameterfv and GetTexParameteriv.
	 */
	public static final int GL_TEXTURE_BORDER_VALUES_NV = 0x871a;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameterfv and
	 * GetTexLevelParameteriv.
	 */
	public static final int GL_TEXTURE_HI_SIZE_NV = 0x871b;
	public static final int GL_TEXTURE_LO_SIZE_NV = 0x871c;
	public static final int GL_TEXTURE_DS_SIZE_NV = 0x871d;
	public static final int GL_TEXTURE_DT_SIZE_NV = 0x871e;
	public static final int GL_TEXTURE_MAG_SIZE_NV = 0x871f;

	private NVTextureShader() {
	}

}
