/* MACHINE GENERATED FILE, DO NOT EDIT */

package org.lwjgl.opengl;

import org.lwjgl.LWJGLException;
import org.lwjgl.BufferChecks;
import org.lwjgl.NondirectBufferWrapper;
import java.nio.*;

public final class GL30 {
	public static final int GL_MAJOR_VERSION = 0x821b;
	public static final int GL_MINOR_VERSION = 0x821c;
	public static final int GL_NUM_EXTENSIONS = 0x821d;
	public static final int GL_CONTEXT_FLAGS = 0x821e;
	public static final int GL_CONTEXT_FLAG_FORWARD_COMPATIBLE_BIT = 0x1;
	public static final int GL_DEPTH_BUFFER = 0x8223;
	public static final int GL_STENCIL_BUFFER = 0x8224;
	public static final int GL_COMPRESSED_RED = 0x8225;
	public static final int GL_COMPRESSED_RG = 0x8226;
	public static final int GL_COMPARE_REF_TO_TEXTURE = 0x884e;
	public static final int GL_CLIP_DISTANCE0 = 0x3000;
	public static final int GL_CLIP_DISTANCE1 = 0x3001;
	public static final int GL_CLIP_DISTANCE2 = 0x3002;
	public static final int GL_CLIP_DISTANCE3 = 0x3003;
	public static final int GL_CLIP_DISTANCE4 = 0x3004;
	public static final int GL_CLIP_DISTANCE5 = 0x3005;
	public static final int GL_MAX_CLIP_DISTANCES = 0xd32;
	public static final int GL_MAX_VARYING_COMPONENTS = 0x8b4b;
	/**
	 * Accepted by the &lt;pname&gt; parameters of GetVertexAttribdv,
	 * GetVertexAttribfv, GetVertexAttribiv, GetVertexAttribIiv, and
	 * GetVertexAttribIuiv:
	 */
	public static final int GL_VERTEX_ATTRIB_ARRAY_INTEGER = 0x88fd;
	/**
	 *Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_BUFFER = 0x8dc2;
	public static final int GL_SAMPLER_CUBE_SHADOW = 0x8dc5;
	public static final int GL_UNSIGNED_INT_VEC2 = 0x8dc6;
	public static final int GL_UNSIGNED_INT_VEC3 = 0x8dc7;
	public static final int GL_UNSIGNED_INT_VEC4 = 0x8dc8;
	public static final int GL_INT_SAMPLER_1D = 0x8dc9;
	public static final int GL_INT_SAMPLER_2D = 0x8dca;
	public static final int GL_INT_SAMPLER_3D = 0x8dcb;
	public static final int GL_INT_SAMPLER_CUBE = 0x8dcc;
	public static final int GL_INT_SAMPLER_2D_RECT = 0x8dcd;
	public static final int GL_INT_SAMPLER_1D_ARRAY = 0x8dce;
	public static final int GL_INT_SAMPLER_2D_ARRAY = 0x8dcf;
	public static final int GL_INT_SAMPLER_BUFFER = 0x8dd0;
	public static final int GL_UNSIGNED_INT_SAMPLER_1D = 0x8dd1;
	public static final int GL_UNSIGNED_INT_SAMPLER_2D = 0x8dd2;
	public static final int GL_UNSIGNED_INT_SAMPLER_3D = 0x8dd3;
	public static final int GL_UNSIGNED_INT_SAMPLER_CUBE = 0x8dd4;
	public static final int GL_UNSIGNED_INT_SAMPLER_2D_RECT = 0x8dd5;
	public static final int GL_UNSIGNED_INT_SAMPLER_1D_ARRAY = 0x8dd6;
	public static final int GL_UNSIGNED_INT_SAMPLER_2D_ARRAY = 0x8dd7;
	public static final int GL_UNSIGNED_INT_SAMPLER_BUFFER = 0x8dd8;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	public static final int GL_MIN_PROGRAM_TEXEL_OFFSET = 0x8904;
	public static final int GL_MAX_PROGRAM_TEXEL_OFFSET = 0x8905;
	/**
	 *Accepted by the &lt;mode&gt; parameter of BeginConditionalRender: 
	 */
	public static final int GL_QUERY_WAIT = 0x8e13;
	public static final int GL_QUERY_NO_WAIT = 0x8e14;
	public static final int GL_QUERY_BY_REGION_WAIT = 0x8e15;
	public static final int GL_QUERY_BY_REGION_NO_WAIT = 0x8e16;
	/**
	 *Accepted by the &lt;access&gt; parameter of MapBufferRange: 
	 */
	public static final int GL_MAP_READ_BIT = 0x1;
	public static final int GL_MAP_WRITE_BIT = 0x2;
	public static final int GL_MAP_INVALIDATE_RANGE_BIT = 0x4;
	public static final int GL_MAP_INVALIDATE_BUFFER_BIT = 0x8;
	public static final int GL_MAP_FLUSH_EXPLICIT_BIT = 0x10;
	public static final int GL_MAP_UNSYNCHRONIZED_BIT = 0x20;
	/**
	 * Accepted by the &lt;target&gt; parameter of ClampColor and the &lt;pname&gt;
	 * parameter of GetBooleanv, GetIntegerv, GetFloatv, and GetDoublev.
	 */
	public static final int GL_CLAMP_VERTEX_COLOR = 0x891a;
	public static final int GL_CLAMP_FRAGMENT_COLOR = 0x891b;
	public static final int GL_CLAMP_READ_COLOR = 0x891c;
	/**
	 *Accepted by the &lt;clamp&gt; parameter of ClampColor. 
	 */
	public static final int GL_FIXED_ONLY = 0x891d;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D, TexImage2D,
	 * TexImage3D, CopyTexImage1D, CopyTexImage2D, and RenderbufferStorageEXT,
	 * and returned in the &lt;data&gt; parameter of GetTexLevelParameter and
	 * GetRenderbufferParameterivEXT:
	 */
	public static final int GL_DEPTH_COMPONENT32F = 0x8dab;
	public static final int GL_DEPTH32F_STENCIL8 = 0x8dac;
	/**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels, TexImage1D,
	 * TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D, and
	 * GetTexImage:
	 */
	public static final int GL_FLOAT_32_UNSIGNED_INT_24_8_REV = 0x8dad;
	/**
	 *Accepted by the &lt;value&gt; parameter of GetTexLevelParameter: 
	 */
	public static final int GL_TEXTURE_RED_TYPE = 0x8c10;
	public static final int GL_TEXTURE_GREEN_TYPE = 0x8c11;
	public static final int GL_TEXTURE_BLUE_TYPE = 0x8c12;
	public static final int GL_TEXTURE_ALPHA_TYPE = 0x8c13;
	public static final int GL_TEXTURE_LUMINANCE_TYPE = 0x8c14;
	public static final int GL_TEXTURE_INTENSITY_TYPE = 0x8c15;
	public static final int GL_TEXTURE_DEPTH_TYPE = 0x8c16;
	/**
	 *Returned by the &lt;params&gt; parameter of GetTexLevelParameter: 
	 */
	public static final int GL_UNSIGNED_NORMALIZED = 0x8c17;
	/**
	 * Accepted by the &lt;internalFormat&gt; parameter of TexImage1D,
	 * TexImage2D, and TexImage3D:
	 */
	public static final int GL_RGBA32F = 0x8814;
	public static final int GL_RGB32F = 0x8815;
	public static final int GL_ALPHA32F = 0x8816;
	public static final int GL_RGBA16F = 0x881a;
	public static final int GL_RGB16F = 0x881b;
	public static final int GL_ALPHA16F = 0x881c;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 * TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 * RenderbufferStorage:
	 */
	public static final int GL_R11F_G11F_B10F = 0x8c3a;
	/**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, GetTexImage, TexImage3D, TexSubImage1D,
	 * TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 * ConvolutionFilter1D, ConvolutionFilter2D, ConvolutionFilter3D,
	 * GetConvolutionFilter, SeparableFilter2D, GetSeparableFilter,
	 * ColorTable, ColorSubTable, and GetColorTable:
	 */
	public static final int GL_UNSIGNED_INT_10F_11F_11F_REV = 0x8c3b;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 * TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 * RenderbufferStorage:
	 */
	public static final int GL_RGB9_E5 = 0x8c3d;
	/**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, GetTexImage, TexImage3D, TexSubImage1D,
	 * TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 * ConvolutionFilter1D, ConvolutionFilter2D, ConvolutionFilter3D,
	 * GetConvolutionFilter, SeparableFilter2D, GetSeparableFilter,
	 * ColorTable, ColorSubTable, and GetColorTable:
	 */
	public static final int GL_UNSIGNED_INT_5_9_9_9_REV = 0x8c3e;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetTexLevelParameterfv and
	 * GetTexLevelParameteriv:
	 */
	public static final int GL_TEXTURE_SHARED_SIZE = 0x8c3f;
	/**
	 * Accepted by the &lt;target&gt; parameter of BindFramebuffer,
	 * CheckFramebufferStatus, FramebufferTexture{1D|2D|3D}, and
	 * FramebufferRenderbuffer:
	 */
	public static final int GL_FRAMEBUFFER = 0x8d40;
	/**
	 * Accepted by the &lt;target&gt; parameter of BindRenderbuffer,
	 * RenderbufferStorage, and GetRenderbufferParameteriv, and
	 * returned by GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_RENDERBUFFER = 0x8d41;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of
	 * RenderbufferStorage:
	 */
	public static final int GL_STENCIL_INDEX1 = 0x8d46;
	public static final int GL_STENCIL_INDEX4 = 0x8d47;
	public static final int GL_STENCIL_INDEX8 = 0x8d48;
	public static final int GL_STENCIL_INDEX16 = 0x8d49;
	/**
	 *Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv: 
	 */
	public static final int GL_RENDERBUFFER_WIDTH = 0x8d42;
	public static final int GL_RENDERBUFFER_HEIGHT = 0x8d43;
	public static final int GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8d44;
	public static final int GL_RENDERBUFFER_RED_SIZE = 0x8d50;
	public static final int GL_RENDERBUFFER_GREEN_SIZE = 0x8d51;
	public static final int GL_RENDERBUFFER_BLUE_SIZE = 0x8d52;
	public static final int GL_RENDERBUFFER_ALPHA_SIZE = 0x8d53;
	public static final int GL_RENDERBUFFER_DEPTH_SIZE = 0x8d54;
	public static final int GL_RENDERBUFFER_STENCIL_SIZE = 0x8d55;
	/**
	 * Accepted by the &lt;pname&gt; parameter of
	 * GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_TYPE = 0x8cd0;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_OBJECT_NAME = 0x8cd1;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LEVEL = 0x8cd2;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_CUBE_MAP_FACE = 0x8cd3;
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_3D_ZOFFSET = 0x8cd4;
	/**
	 * Accepted by the &lt;attachment&gt; parameter of
	 * FramebufferTexture{1D|2D|3D}, FramebufferRenderbuffer, and
	 * GetFramebufferAttachmentParameteriv
	 */
	public static final int GL_COLOR_ATTACHMENT0 = 0x8ce0;
	public static final int GL_COLOR_ATTACHMENT1 = 0x8ce1;
	public static final int GL_COLOR_ATTACHMENT2 = 0x8ce2;
	public static final int GL_COLOR_ATTACHMENT3 = 0x8ce3;
	public static final int GL_COLOR_ATTACHMENT4 = 0x8ce4;
	public static final int GL_COLOR_ATTACHMENT5 = 0x8ce5;
	public static final int GL_COLOR_ATTACHMENT6 = 0x8ce6;
	public static final int GL_COLOR_ATTACHMENT7 = 0x8ce7;
	public static final int GL_COLOR_ATTACHMENT8 = 0x8ce8;
	public static final int GL_COLOR_ATTACHMENT9 = 0x8ce9;
	public static final int GL_COLOR_ATTACHMENT10 = 0x8cea;
	public static final int GL_COLOR_ATTACHMENT11 = 0x8ceb;
	public static final int GL_COLOR_ATTACHMENT12 = 0x8cec;
	public static final int GL_COLOR_ATTACHMENT13 = 0x8ced;
	public static final int GL_COLOR_ATTACHMENT14 = 0x8cee;
	public static final int GL_COLOR_ATTACHMENT15 = 0x8cef;
	public static final int GL_DEPTH_ATTACHMENT = 0x8d00;
	public static final int GL_STENCIL_ATTACHMENT = 0x8d20;
	/**
	 *Returned by CheckFramebufferStatus(): 
	 */
	public static final int GL_FRAMEBUFFER_COMPLETE = 0x8cd5;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8cd6;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8cd7;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8cd9;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_FORMATS = 0x8cda;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER = 0x8cdb;
	public static final int GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER = 0x8cdc;
	public static final int GL_FRAMEBUFFER_UNSUPPORTED = 0x8cdd;
	/**
	 *Accepted by GetIntegerv(): 
	 */
	public static final int GL_FRAMEBUFFER_BINDING = 0x8ca6;
	public static final int GL_RENDERBUFFER_BINDING = 0x8ca7;
	public static final int GL_MAX_COLOR_ATTACHMENTS = 0x8cdf;
	public static final int GL_MAX_RENDERBUFFER_SIZE = 0x84e8;
	/**
	 *Returned by GetError(): 
	 */
	public static final int GL_INVALID_FRAMEBUFFER_OPERATION = 0x506;
	/**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, TexImage3D, GetTexImage, TexSubImage1D,
	 * TexSubImage2D, TexSubImage3D, GetHistogram, GetMinmax,
	 * ConvolutionFilter1D, ConvolutionFilter2D, GetConvolutionFilter,
	 * SeparableFilter2D, GetSeparableFilter, ColorTable, ColorSubTable,
	 * and GetColorTable:
	 * <p/>
	 * Accepted by the &lt;type&gt; argument of VertexPointer, NormalPointer,
	 * ColorPointer, SecondaryColorPointer, FogCoordPointer, TexCoordPointer,
	 * and VertexAttribPointer:
	 */
	public static final int GL_HALF_FLOAT = 0x140b;
	/**
	 *Accepted by the &lt;pname&gt; parameter of GetRenderbufferParameteriv. 
	 */
	public static final int GL_RENDERBUFFER_SAMPLES = 0x8cab;
	/**
	 *Returned by CheckFramebufferStatus. 
	 */
	public static final int GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE = 0x8d56;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev.
	 */
	public static final int GL_MAX_SAMPLES = 0x8d57;
	/**
	 * Accepted by the &lt;target&gt; parameter of BindFramebuffer,
	 * CheckFramebufferStatus, FramebufferTexture{1D|2D|3D},
	 * FramebufferRenderbuffer, and
	 * GetFramebufferAttachmentParameteriv.
	 */
	public static final int GL_READ_FRAMEBUFFER = 0x8ca8;
	public static final int GL_DRAW_FRAMEBUFFER = 0x8ca9;
	/**
	 *Accepted by the &lt;pname&gt; parameters of GetIntegerv, GetFloatv, and GetDoublev. 
	 */
	public static final int GL_DRAW_FRAMEBUFFER_BINDING = 0x8ca6;
	public static final int GL_READ_FRAMEBUFFER_BINDING = 0x8caa;
	/**
	 * Accepted by the &lt;pname&gt; parameters of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	public static final int GL_RGBA_INTEGER_MODE = 0x8d9e;
	/**
	 * Accepted by the &lt;internalFormat&gt; parameter of TexImage1D,
	 * TexImage2D, and TexImage3D:
	 */
	public static final int GL_RGBA32UI = 0x8d70;
	public static final int GL_RGB32UI = 0x8d71;
	public static final int GL_ALPHA32UI = 0x8d72;
	public static final int GL_RGBA16UI = 0x8d76;
	public static final int GL_RGB16UI = 0x8d77;
	public static final int GL_ALPHA16UI = 0x8d78;
	public static final int GL_RGBA8UI = 0x8d7c;
	public static final int GL_RGB8UI = 0x8d7d;
	public static final int GL_ALPHA8UI = 0x8d7e;
	public static final int GL_RGBA32I = 0x8d82;
	public static final int GL_RGB32I = 0x8d83;
	public static final int GL_ALPHA32I = 0x8d84;
	public static final int GL_RGBA16I = 0x8d88;
	public static final int GL_RGB16I = 0x8d89;
	public static final int GL_ALPHA16I = 0x8d8a;
	public static final int GL_RGBA8I = 0x8d8e;
	public static final int GL_RGB8I = 0x8d8f;
	public static final int GL_ALPHA8I = 0x8d90;
	/**
	 * Accepted by the &lt;format&gt; parameter of TexImage1D, TexImage2D,
	 * TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D,
	 * DrawPixels and ReadPixels:
	 */
	public static final int GL_RED_INTEGER = 0x8d94;
	public static final int GL_GREEN_INTEGER = 0x8d95;
	public static final int GL_BLUE_INTEGER = 0x8d96;
	public static final int GL_ALPHA_INTEGER = 0x8d97;
	public static final int GL_RGB_INTEGER = 0x8d98;
	public static final int GL_RGBA_INTEGER = 0x8d99;
	public static final int GL_BGR_INTEGER = 0x8d9a;
	public static final int GL_BGRA_INTEGER = 0x8d9b;
	/**
	 * Accepted by the &lt;target&gt; parameter of TexParameteri, TexParameteriv,
	 * TexParameterf, TexParameterfv, and BindTexture:
	 */
	public static final int GL_TEXTURE_1D_ARRAY = 0x8c18;
	public static final int GL_TEXTURE_2D_ARRAY = 0x8c1a;
	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage3D, TexSubImage3D,
	 * CopyTexSubImage3D, CompressedTexImage3D, and CompressedTexSubImage3D:
	 */
	public static final int GL_PROXY_TEXTURE_2D_ARRAY = 0x8c1b;
	/**
	 * Accepted by the &lt;target&gt; parameter of TexImage2D, TexSubImage2D,
	 * CopyTexImage2D, CopyTexSubImage2D, CompressedTexImage2D, and
	 * CompressedTexSubImage2D:
	 */
	public static final int GL_PROXY_TEXTURE_1D_ARRAY = 0x8c19;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv
	 * and GetFloatv:
	 */
	public static final int GL_TEXTURE_BINDING_1D_ARRAY = 0x8c1c;
	public static final int GL_TEXTURE_BINDING_2D_ARRAY = 0x8c1d;
	public static final int GL_MAX_ARRAY_TEXTURE_LAYERS = 0x88ff;
	/**
	 * Accepted by the &lt;param&gt; parameter of TexParameterf, TexParameteri,
	 * TexParameterfv, and TexParameteriv when the &lt;pname&gt; parameter is
	 * TEXTURE_COMPARE_MODE_ARB:
	 */
	public static final int GL_COMPARE_REF_DEPTH_TO_TEXTURE = 0x884e;
	/**
	 * Accepted by the &lt;pname&gt; parameter of
	 * GetFramebufferAttachmentParameteriv:
	 */
	public static final int GL_FRAMEBUFFER_ATTACHMENT_TEXTURE_LAYER = 0x8cd4;
	/**
	 *Returned by the &lt;type&gt; parameter of GetActiveUniform: 
	 */
	public static final int GL_SAMPLER_1D_ARRAY = 0x8dc0;
	public static final int GL_SAMPLER_2D_ARRAY = 0x8dc1;
	public static final int GL_SAMPLER_1D_ARRAY_SHADOW = 0x8dc3;
	public static final int GL_SAMPLER_2D_ARRAY_SHADOW = 0x8dc4;
	/**
	 * Accepted by the &lt;format&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D,
	 * TexSubImage3D, and GetTexImage, by the &lt;type&gt; parameter of
	 * CopyPixels, by the &lt;internalformat&gt; parameter of TexImage1D,
	 * TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 * RenderbufferStorage, and returned in the &lt;data&gt; parameter of
	 * GetTexLevelParameter and GetRenderbufferParameteriv.
	 */
	public static final int GL_DEPTH_STENCIL = 0x84f9;
	/**
	 * Accepted by the &lt;type&gt; parameter of DrawPixels, ReadPixels,
	 * TexImage1D, TexImage2D, TexImage3D, TexSubImage1D, TexSubImage2D,
	 * TexSubImage3D, and GetTexImage.
	 */
	public static final int GL_UNSIGNED_INT_24_8 = 0x84fa;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage1D,
	 * TexImage2D, TexImage3D, CopyTexImage1D, CopyTexImage2D, and
	 * RenderbufferStorage, and returned in the &lt;data&gt; parameter of
	 * GetTexLevelParameter and GetRenderbufferParameteriv.
	 */
	public static final int GL_DEPTH24_STENCIL8 = 0x88f0;
	/**
	 *Accepted by the &lt;value&gt; parameter of GetTexLevelParameter. 
	 */
	public static final int GL_TEXTURE_STENCIL_SIZE = 0x88f1;
	/**
	 * Accepted by the &lt;internalformat&gt; parameter of TexImage2D,
	 * CopyTexImage2D, and CompressedTexImage2D and the &lt;format&gt; parameter
	 * of CompressedTexSubImage2D:
	 */
	public static final int GL_COMPRESSED_RED_RGTC1 = 0x8dbb;
	public static final int GL_COMPRESSED_SIGNED_RED_RGTC1 = 0x8dbc;
	public static final int GL_COMPRESSED_RED_GREEN_RGTC2 = 0x8dbd;
	public static final int GL_COMPRESSED_SIGNED_RED_GREEN_RGTC2 = 0x8dbe;
	/**
	 * Accepted by the &lt;internalFormat&gt; parameter of TexImage1D, TexImage2D,
	 * TexImage3D, CopyTexImage1D, and CopyTexImage2D:
	 */
	public static final int GL_R8 = 0x8229;
	public static final int GL_R16 = 0x822a;
	public static final int GL_RG8 = 0x822b;
	public static final int GL_RG16 = 0x822c;
	public static final int GL_R16F = 0x822d;
	public static final int GL_R32F = 0x822e;
	public static final int GL_RG16F = 0x822f;
	public static final int GL_RG32F = 0x8230;
	public static final int GL_R8I = 0x8231;
	public static final int GL_R8UI = 0x8232;
	public static final int GL_R16I = 0x8233;
	public static final int GL_R16UI = 0x8234;
	public static final int GL_R32I = 0x8235;
	public static final int GL_R32UI = 0x8236;
	public static final int GL_RG8I = 0x8237;
	public static final int GL_RG8UI = 0x8238;
	public static final int GL_RG16I = 0x8239;
	public static final int GL_RG16UI = 0x823a;
	public static final int GL_RG32I = 0x823b;
	public static final int GL_RG32UI = 0x823c;
	/**
	 * Accepted by the &lt;format&gt; parameter of TexImage3D, TexImage2D,
	 * TexImage3D, TexSubImage1D, TexSubImage2D, TexSubImage3D,
	 * DrawPixels and ReadPixels:
	 */
	public static final int GL_RG = 0x8227;
	public static final int GL_RG_INTEGER = 0x8228;
	/**
	 * Accepted by the &lt;param&gt; parameter of the TexParameter{if}*
	 * functions when &lt;pname&gt; is DEPTH_TEXTURE_MODE:
	 */
	public static final int GL_RED = 0x1903;
	/**
	 * Accepted by the &lt;target&gt; parameters of BindBuffer, BufferData,
	 * BufferSubData, MapBuffer, UnmapBuffer, GetBufferSubData,
	 * GetBufferPointerv, BindBufferRange, BindBufferOffset and
	 * BindBufferBase:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER = 0x8c8e;
	/**
	 * Accepted by the &lt;param&gt; parameter of GetIntegerIndexedv and
	 * GetBooleanIndexedv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_START = 0x8c84;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_SIZE = 0x8c85;
	/**
	 * Accepted by the &lt;param&gt; parameter of GetIntegerIndexedv and
	 * GetBooleanIndexedv, and by the &lt;pname&gt; parameter of GetBooleanv,
	 * GetDoublev, GetIntegerv, and GetFloatv:
	 */
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_BINDING = 0x8c8f;
	/**
	 *Accepted by the &lt;bufferMode&gt; parameter of TransformFeedbackVaryings: 
	 */
	public static final int GL_INTERLEAVED_ATTRIBS = 0x8c8c;
	public static final int GL_SEPARATE_ATTRIBS = 0x8c8d;
	/**
	 * Accepted by the &lt;target&gt; parameter of BeginQuery, EndQuery, and
	 * GetQueryiv:
	 */
	public static final int GL_PRIMITIVES_GENERATED = 0x8c87;
	public static final int GL_TRANSFORM_FEEDBACK_PRIMITIVES_WRITTEN = 0x8c88;
	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled, and by
	 * the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv, and
	 * GetDoublev:
	 */
	public static final int GL_RASTERIZER_DISCARD = 0x8c89;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetDoublev, GetIntegerv,
	 * and GetFloatv:
	 */
	public static final int GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS = 0x8c8a;
	public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS = 0x8c8b;
	public static final int GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS = 0x8c80;
	/**
	 *Accepted by the &lt;pname&gt; parameter of GetProgramiv: 
	 */
	public static final int GL_TRANSFORM_FEEDBACK_VARYINGS = 0x8c83;
	public static final int GL_TRANSFORM_FEEDBACK_BUFFER_MODE = 0x8c7f;
	public static final int GL_TRANSFORM_FEEDBACK_VARYING_MAX_LENGTH = 0x8c76;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	public static final int GL_VERTEX_ARRAY_BINDING = 0x85b5;
	/**
	 * Accepted by the &lt;cap&gt; parameter of Enable, Disable, and IsEnabled,
	 * and by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv, GetFloatv,
	 * and GetDoublev:
	 */
	public static final int GL_FRAMEBUFFER_SRGB = 0x8db9;
	/**
	 * Accepted by the &lt;pname&gt; parameter of GetBooleanv, GetIntegerv,
	 * GetFloatv, and GetDoublev:
	 */
	public static final int GL_FRAMEBUFFER_SRGB_CAPABLE = 0x8dba;

	private GL30() {
	}


	public static java.lang.String glGetStringi(int name, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetStringi_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		java.lang.String __result = nglGetStringi(name, index, function_pointer);
		return __result;
	}
	private static native java.lang.String nglGetStringi(int name, int index, long function_pointer);

	public static void glClearBufferfv(int buffer, FloatBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glClearBufferfv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapBuffer(value, 4);
		nglClearBufferfv(buffer, value, value.position(), function_pointer);
	}
	private static native void nglClearBufferfv(int buffer, FloatBuffer value, int value_position, long function_pointer);

	public static void glClearBufferiv(int buffer, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glClearBufferiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapBuffer(value, 4);
		nglClearBufferiv(buffer, value, value.position(), function_pointer);
	}
	private static native void nglClearBufferiv(int buffer, IntBuffer value, int value_position, long function_pointer);

	public static void glClearBufferuiv(int buffer, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glClearBufferuiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapBuffer(value, 4);
		nglClearBufferuiv(buffer, value, value.position(), function_pointer);
	}
	private static native void nglClearBufferuiv(int buffer, IntBuffer value, int value_position, long function_pointer);

	public static void glClearBufferfi(int buffer, float depth, int stencil) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glClearBufferfi_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClearBufferfi(buffer, depth, stencil, function_pointer);
	}
	private static native void nglClearBufferfi(int buffer, float depth, int stencil, long function_pointer);

	public static void glVertexAttribI1i(int index, int x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI1i_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI1i(index, x, function_pointer);
	}
	private static native void nglVertexAttribI1i(int index, int x, long function_pointer);

	public static void glVertexAttribI2i(int index, int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI2i_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI2i(index, x, y, function_pointer);
	}
	private static native void nglVertexAttribI2i(int index, int x, int y, long function_pointer);

	public static void glVertexAttribI3i(int index, int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI3i_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI3i(index, x, y, z, function_pointer);
	}
	private static native void nglVertexAttribI3i(int index, int x, int y, int z, long function_pointer);

	public static void glVertexAttribI4i(int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI4i_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI4i(index, x, y, z, w, function_pointer);
	}
	private static native void nglVertexAttribI4i(int index, int x, int y, int z, int w, long function_pointer);

	public static void glVertexAttribI1ui(int index, int x) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI1ui_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI1ui(index, x, function_pointer);
	}
	private static native void nglVertexAttribI1ui(int index, int x, long function_pointer);

	public static void glVertexAttribI2ui(int index, int x, int y) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI2ui_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI2ui(index, x, y, function_pointer);
	}
	private static native void nglVertexAttribI2ui(int index, int x, int y, long function_pointer);

	public static void glVertexAttribI3ui(int index, int x, int y, int z) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI3ui_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI3ui(index, x, y, z, function_pointer);
	}
	private static native void nglVertexAttribI3ui(int index, int x, int y, int z, long function_pointer);

	public static void glVertexAttribI4ui(int index, int x, int y, int z, int w) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI4ui_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglVertexAttribI4ui(index, x, y, z, w, function_pointer);
	}
	private static native void nglVertexAttribI4ui(int index, int x, int y, int z, int w, long function_pointer);

	public static void glVertexAttribI1(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI1iv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 1);
		nglVertexAttribI1iv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI1iv(int index, IntBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI2(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI2iv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 2);
		nglVertexAttribI2iv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI2iv(int index, IntBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI3(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI3iv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 3);
		nglVertexAttribI3iv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI3iv(int index, IntBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI4(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI4iv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 4);
		nglVertexAttribI4iv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI4iv(int index, IntBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI1u(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI1uiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 1);
		nglVertexAttribI1uiv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI1uiv(int index, IntBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI2u(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI2uiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 2);
		nglVertexAttribI2uiv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI2uiv(int index, IntBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI3u(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI3uiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 3);
		nglVertexAttribI3uiv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI3uiv(int index, IntBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI4u(int index, IntBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI4uiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 4);
		nglVertexAttribI4uiv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI4uiv(int index, IntBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI4(int index, ByteBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI4bv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 4);
		nglVertexAttribI4bv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI4bv(int index, ByteBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI4(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI4sv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 4);
		nglVertexAttribI4sv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI4sv(int index, ShortBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI4u(int index, ByteBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI4ubv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 4);
		nglVertexAttribI4ubv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI4ubv(int index, ByteBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribI4u(int index, ShortBuffer v) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribI4usv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		v = NondirectBufferWrapper.wrapBuffer(v, 4);
		nglVertexAttribI4usv(index, v, v.position(), function_pointer);
	}
	private static native void nglVertexAttribI4usv(int index, ShortBuffer v, int v_position, long function_pointer);

	public static void glVertexAttribIPointer(int index, int size, int type, int stride, ByteBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribIPointer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		GLChecks.getReferences(caps).GL30_glVertexAttribIPointer_buffer = buffer;
		nglVertexAttribIPointer(index, size, type, stride, buffer, buffer.position(), function_pointer);
	}
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, IntBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribIPointer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		GLChecks.getReferences(caps).GL30_glVertexAttribIPointer_buffer = buffer;
		nglVertexAttribIPointer(index, size, type, stride, buffer, buffer.position() << 2, function_pointer);
	}
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, ShortBuffer buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribIPointer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOdisabled(caps);
		BufferChecks.checkDirect(buffer);
		GLChecks.getReferences(caps).GL30_glVertexAttribIPointer_buffer = buffer;
		nglVertexAttribIPointer(index, size, type, stride, buffer, buffer.position() << 1, function_pointer);
	}
	private static native void nglVertexAttribIPointer(int index, int size, int type, int stride, Buffer buffer, int buffer_position, long function_pointer);
	public static void glVertexAttribIPointer(int index, int size, int type, int stride, long buffer_buffer_offset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glVertexAttribIPointer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		GLChecks.ensureArrayVBOenabled(caps);
		nglVertexAttribIPointerBO(index, size, type, stride, buffer_buffer_offset, function_pointer);
	}
	private static native void nglVertexAttribIPointerBO(int index, int size, int type, int stride, long buffer_buffer_offset, long function_pointer);

	public static void glGetVertexAttribI(int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetVertexAttribIiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetVertexAttribIiv(index, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetVertexAttribIiv(int index, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetVertexAttribIu(int index, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetVertexAttribIuiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetVertexAttribIuiv(index, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetVertexAttribIuiv(int index, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glUniform1ui(int location, int v0) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glUniform1ui_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform1ui(location, v0, function_pointer);
	}
	private static native void nglUniform1ui(int location, int v0, long function_pointer);

	public static void glUniform2ui(int location, int v0, int v1) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glUniform2ui_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform2ui(location, v0, v1, function_pointer);
	}
	private static native void nglUniform2ui(int location, int v0, int v1, long function_pointer);

	public static void glUniform3ui(int location, int v0, int v1, int v2) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glUniform3ui_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform3ui(location, v0, v1, v2, function_pointer);
	}
	private static native void nglUniform3ui(int location, int v0, int v1, int v2, long function_pointer);

	public static void glUniform4ui(int location, int v0, int v1, int v2, int v3) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glUniform4ui_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglUniform4ui(location, v0, v1, v2, v3, function_pointer);
	}
	private static native void nglUniform4ui(int location, int v0, int v1, int v2, int v3, long function_pointer);

	public static void glUniform1u(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glUniform1uiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglUniform1uiv(location, (value.remaining()), value, value.position(), function_pointer);
	}
	private static native void nglUniform1uiv(int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glUniform2u(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glUniform2uiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglUniform2uiv(location, (value.remaining()) >> 1, value, value.position(), function_pointer);
	}
	private static native void nglUniform2uiv(int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glUniform3u(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glUniform3uiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglUniform3uiv(location, (value.remaining()) / 3, value, value.position(), function_pointer);
	}
	private static native void nglUniform3uiv(int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glUniform4u(int location, IntBuffer value) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glUniform4uiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		value = NondirectBufferWrapper.wrapDirect(value);
		nglUniform4uiv(location, (value.remaining()) >> 2, value, value.position(), function_pointer);
	}
	private static native void nglUniform4uiv(int location, int count, IntBuffer value, int value_position, long function_pointer);

	public static void glGetUniformu(int program, int location, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetUniformuiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyDirect(params);
		nglGetUniformuiv(program, location, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetUniformuiv(int program, int location, IntBuffer params, int params_position, long function_pointer);

	public static void glBindFragDataLocation(int program, int colorNumber, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBindFragDataLocation_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		name = NondirectBufferWrapper.wrapDirect(name);
		BufferChecks.checkNullTerminated(name);
		nglBindFragDataLocation(program, colorNumber, name, name.position(), function_pointer);
	}
	private static native void nglBindFragDataLocation(int program, int colorNumber, ByteBuffer name, int name_position, long function_pointer);

	public static int glGetFragDataLocation(int program, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetFragDataLocation_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		name = NondirectBufferWrapper.wrapDirect(name);
		BufferChecks.checkNullTerminated(name);
		int __result = nglGetFragDataLocation(program, name, name.position(), function_pointer);
		return __result;
	}
	private static native int nglGetFragDataLocation(int program, ByteBuffer name, int name_position, long function_pointer);

	public static void glBeginConditionalRender(int id, int mode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBeginConditionalRender_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginConditionalRender(id, mode, function_pointer);
	}
	private static native void nglBeginConditionalRender(int id, int mode, long function_pointer);

	public static void glEndConditionalRender() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glEndConditionalRender_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndConditionalRender(function_pointer);
	}
	private static native void nglEndConditionalRender(long function_pointer);

	public static java.nio.ByteBuffer glMapBufferRange(int target, long offset, long length, int access, long result_size, java.nio.ByteBuffer old_buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glMapBufferRange_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		if (old_buffer != null)
			BufferChecks.checkDirect(old_buffer);
		java.nio.ByteBuffer __result = nglMapBufferRange(target, offset, length, access, result_size, old_buffer, function_pointer);
		return __result;
	}
	private static native java.nio.ByteBuffer nglMapBufferRange(int target, long offset, long length, int access, long result_size, java.nio.ByteBuffer old_buffer, long function_pointer);

	public static void glFlushMappedBufferRange(int target, long offset, long length) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glFlushMappedBufferRange_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFlushMappedBufferRange(target, offset, length, function_pointer);
	}
	private static native void nglFlushMappedBufferRange(int target, long offset, long length, long function_pointer);

	public static void glClampColorARB(int target, int clamp) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glClampColorARB_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglClampColorARB(target, clamp, function_pointer);
	}
	private static native void nglClampColorARB(int target, int clamp, long function_pointer);

	public static boolean glIsRenderbuffer(int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glIsRenderbuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsRenderbuffer(renderbuffer, function_pointer);
		return __result;
	}
	private static native boolean nglIsRenderbuffer(int renderbuffer, long function_pointer);

	public static void glBindRenderbuffer(int target, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBindRenderbuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindRenderbuffer(target, renderbuffer, function_pointer);
	}
	private static native void nglBindRenderbuffer(int target, int renderbuffer, long function_pointer);

	public static void glDeleteRenderbuffers(IntBuffer renderbuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glDeleteRenderbuffers_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		renderbuffers = NondirectBufferWrapper.wrapDirect(renderbuffers);
		nglDeleteRenderbuffers((renderbuffers.remaining()), renderbuffers, renderbuffers.position(), function_pointer);
	}
	private static native void nglDeleteRenderbuffers(int n, IntBuffer renderbuffers, int renderbuffers_position, long function_pointer);

	public static void glGenRenderbuffers(IntBuffer renderbuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGenRenderbuffers_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer renderbuffers_saved = renderbuffers;
		renderbuffers = NondirectBufferWrapper.wrapNoCopyDirect(renderbuffers);
		nglGenRenderbuffers((renderbuffers.remaining()), renderbuffers, renderbuffers.position(), function_pointer);
		NondirectBufferWrapper.copy(renderbuffers, renderbuffers_saved);
	}
	private static native void nglGenRenderbuffers(int n, IntBuffer renderbuffers, int renderbuffers_position, long function_pointer);

	public static void glRenderbufferStorage(int target, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glRenderbufferStorage_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRenderbufferStorage(target, internalformat, width, height, function_pointer);
	}
	private static native void nglRenderbufferStorage(int target, int internalformat, int width, int height, long function_pointer);

	public static void glGetRenderbufferParameter(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetRenderbufferParameteriv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetRenderbufferParameteriv(target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetRenderbufferParameteriv(int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static boolean glIsFramebuffer(int framebuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glIsFramebuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsFramebuffer(framebuffer, function_pointer);
		return __result;
	}
	private static native boolean nglIsFramebuffer(int framebuffer, long function_pointer);

	public static void glBindFramebuffer(int target, int framebuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBindFramebuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindFramebuffer(target, framebuffer, function_pointer);
	}
	private static native void nglBindFramebuffer(int target, int framebuffer, long function_pointer);

	public static void glDeleteFramebuffers(IntBuffer framebuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glDeleteFramebuffers_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		framebuffers = NondirectBufferWrapper.wrapDirect(framebuffers);
		nglDeleteFramebuffers((framebuffers.remaining()), framebuffers, framebuffers.position(), function_pointer);
	}
	private static native void nglDeleteFramebuffers(int n, IntBuffer framebuffers, int framebuffers_position, long function_pointer);

	public static void glGenFramebuffers(IntBuffer framebuffers) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGenFramebuffers_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer framebuffers_saved = framebuffers;
		framebuffers = NondirectBufferWrapper.wrapNoCopyDirect(framebuffers);
		nglGenFramebuffers((framebuffers.remaining()), framebuffers, framebuffers.position(), function_pointer);
		NondirectBufferWrapper.copy(framebuffers, framebuffers_saved);
	}
	private static native void nglGenFramebuffers(int n, IntBuffer framebuffers, int framebuffers_position, long function_pointer);

	public static int glCheckFramebufferStatus(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glCheckFramebufferStatus_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		int __result = nglCheckFramebufferStatus(target, function_pointer);
		return __result;
	}
	private static native int nglCheckFramebufferStatus(int target, long function_pointer);

	public static void glFramebufferTexture1D(int target, int attachment, int textarget, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glFramebufferTexture1D_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture1D(target, attachment, textarget, texture, level, function_pointer);
	}
	private static native void nglFramebufferTexture1D(int target, int attachment, int textarget, int texture, int level, long function_pointer);

	public static void glFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glFramebufferTexture2D_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture2D(target, attachment, textarget, texture, level, function_pointer);
	}
	private static native void nglFramebufferTexture2D(int target, int attachment, int textarget, int texture, int level, long function_pointer);

	public static void glFramebufferTexture3D(int target, int attachment, int textarget, int texture, int level, int zoffset) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glFramebufferTexture3D_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTexture3D(target, attachment, textarget, texture, level, zoffset, function_pointer);
	}
	private static native void nglFramebufferTexture3D(int target, int attachment, int textarget, int texture, int level, int zoffset, long function_pointer);

	public static void glFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glFramebufferRenderbuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferRenderbuffer(target, attachment, renderbuffertarget, renderbuffer, function_pointer);
	}
	private static native void nglFramebufferRenderbuffer(int target, int attachment, int renderbuffertarget, int renderbuffer, long function_pointer);

	public static void glGetFramebufferAttachmentParameter(int target, int attachment, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetFramebufferAttachmentParameteriv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetFramebufferAttachmentParameteriv(target, attachment, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetFramebufferAttachmentParameteriv(int target, int attachment, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGenerateMipmap(int target) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGenerateMipmap_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglGenerateMipmap(target, function_pointer);
	}
	private static native void nglGenerateMipmap(int target, long function_pointer);

	/**
	 * Establishes the data storage, format, dimensions, and number of
	 * samples of a renderbuffer object's image.
	 */
	public static void glRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glRenderbufferStorageMultisample_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglRenderbufferStorageMultisample(target, samples, internalformat, width, height, function_pointer);
	}
	private static native void nglRenderbufferStorageMultisample(int target, int samples, int internalformat, int width, int height, long function_pointer);

	/**
	 * Transfers a rectangle of pixel values from one
	 * region of the read framebuffer to another in the draw framebuffer.
	 * &lt;mask&gt; is the bitwise OR of a number of values indicating which
	 * buffers are to be copied. The values are COLOR_BUFFER_BIT,
	 * DEPTH_BUFFER_BIT, and STENCIL_BUFFER_BIT.
	 * The pixels corresponding to these buffers are
	 * copied from the source rectangle, bound by the locations (srcX0,
	 * srcY0) and (srcX1, srcY1) inclusive, to the destination rectangle,
	 * bound by the locations (dstX0, dstY0) and (dstX1, dstY1)
	 * inclusive.
	 * If the source and destination rectangle dimensions do not match,
	 * the source image is stretched to fit the destination
	 * rectangle. &lt;filter&gt; must be LINEAR or NEAREST and specifies the
	 * method of interpolation to be applied if the image is
	 * stretched.
	 */
	public static void glBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBlitFramebuffer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBlitFramebuffer(srcX0, srcY0, srcX1, srcY1, dstX0, dstY0, dstX1, dstY1, mask, filter, function_pointer);
	}
	private static native void nglBlitFramebuffer(int srcX0, int srcY0, int srcX1, int srcY1, int dstX0, int dstY0, int dstX1, int dstY1, int mask, int filter, long function_pointer);

	public static void glTexParameterI(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glTexParameterIiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglTexParameterIiv(target, pname, params, params.position(), function_pointer);
	}
	private static native void nglTexParameterIiv(int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glTexParameterIu(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glTexParameterIuiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		params = NondirectBufferWrapper.wrapBuffer(params, 4);
		nglTexParameterIuiv(target, pname, params, params.position(), function_pointer);
	}
	private static native void nglTexParameterIuiv(int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetTexParameterI(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetTexParameterIiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetTexParameterIiv(target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetTexParameterIiv(int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glGetTexParameterIu(int target, int pname, IntBuffer params) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetTexParameterIuiv_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer params_saved = params;
		params = NondirectBufferWrapper.wrapNoCopyBuffer(params, 4);
		nglGetTexParameterIuiv(target, pname, params, params.position(), function_pointer);
		NondirectBufferWrapper.copy(params, params_saved);
	}
	private static native void nglGetTexParameterIuiv(int target, int pname, IntBuffer params, int params_position, long function_pointer);

	public static void glFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glFramebufferTextureLayer_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglFramebufferTextureLayer(target, attachment, texture, level, layer, function_pointer);
	}
	private static native void nglFramebufferTextureLayer(int target, int attachment, int texture, int level, int layer, long function_pointer);

	public static void glColorMaski(int buf, boolean r, boolean g, boolean b, boolean a) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glColorMaski_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglColorMaski(buf, r, g, b, a, function_pointer);
	}
	private static native void nglColorMaski(int buf, boolean r, boolean g, boolean b, boolean a, long function_pointer);

	public static void glGetBooleani_(int value, int index, ByteBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetBooleani_v_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		ByteBuffer data_saved = data;
		data = NondirectBufferWrapper.wrapNoCopyBuffer(data, 4);
		nglGetBooleani_v(value, index, data, data.position(), function_pointer);
		NondirectBufferWrapper.copy(data, data_saved);
	}
	private static native void nglGetBooleani_v(int value, int index, ByteBuffer data, int data_position, long function_pointer);

	public static void glGetIntegeri(int value, int index, IntBuffer data) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetIntegeri_v_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer data_saved = data;
		data = NondirectBufferWrapper.wrapNoCopyBuffer(data, 4);
		nglGetIntegeri_v(value, index, data, data.position(), function_pointer);
		NondirectBufferWrapper.copy(data, data_saved);
	}
	private static native void nglGetIntegeri_v(int value, int index, IntBuffer data, int data_position, long function_pointer);

	public static void glEnablei(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glEnablei_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEnablei(target, index, function_pointer);
	}
	private static native void nglEnablei(int target, int index, long function_pointer);

	public static void glDisablei(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glDisablei_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglDisablei(target, index, function_pointer);
	}
	private static native void nglDisablei(int target, int index, long function_pointer);

	public static boolean glIsEnabledi(int target, int index) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glIsEnabledi_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsEnabledi(target, index, function_pointer);
		return __result;
	}
	private static native boolean nglIsEnabledi(int target, int index, long function_pointer);

	public static void glBindBufferRange(int target, int index, int buffer, long offset, long size) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBindBufferRange_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferRange(target, index, buffer, offset, size, function_pointer);
	}
	private static native void nglBindBufferRange(int target, int index, int buffer, long offset, long size, long function_pointer);

	public static void glBindBufferBase(int target, int index, int buffer) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBindBufferBase_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindBufferBase(target, index, buffer, function_pointer);
	}
	private static native void nglBindBufferBase(int target, int index, int buffer, long function_pointer);

	public static void glBeginTransformFeedback(int primitiveMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBeginTransformFeedback_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBeginTransformFeedback(primitiveMode, function_pointer);
	}
	private static native void nglBeginTransformFeedback(int primitiveMode, long function_pointer);

	public static void glEndTransformFeedback() {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glEndTransformFeedback_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglEndTransformFeedback(function_pointer);
	}
	private static native void nglEndTransformFeedback(long function_pointer);

	public static void glTransformFeedbackVaryings(int program, int count, ByteBuffer varyings, int bufferMode) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glTransformFeedbackVaryings_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		varyings = NondirectBufferWrapper.wrapDirect(varyings);
		BufferChecks.checkNullTerminated(varyings);
		nglTransformFeedbackVaryings(program, count, varyings, varyings.position(), bufferMode, function_pointer);
	}
	private static native void nglTransformFeedbackVaryings(int program, int count, ByteBuffer varyings, int varyings_position, int bufferMode, long function_pointer);

	public static void glGetTransformFeedbackVarying(int program, int index, IntBuffer length, IntBuffer size, IntBuffer type, ByteBuffer name) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGetTransformFeedbackVarying_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer length_saved = length;
		if (length != null)
			length = NondirectBufferWrapper.wrapNoCopyBuffer(length, 1);
		IntBuffer size_saved = size;
		if (size != null)
			size = NondirectBufferWrapper.wrapNoCopyBuffer(size, 1);
		IntBuffer type_saved = type;
		if (type != null)
			type = NondirectBufferWrapper.wrapNoCopyBuffer(type, 1);
		name = NondirectBufferWrapper.wrapDirect(name);
		nglGetTransformFeedbackVarying(program, index, (name.remaining()), length, length != null ? length.position() : 0, size, size != null ? size.position() : 0, type, type != null ? type.position() : 0, name, name.position(), function_pointer);
		NondirectBufferWrapper.copy(length, length_saved);
		NondirectBufferWrapper.copy(size, size_saved);
		NondirectBufferWrapper.copy(type, type_saved);
	}
	private static native void nglGetTransformFeedbackVarying(int program, int index, int bufSize, IntBuffer length, int length_position, IntBuffer size, int size_position, IntBuffer type, int type_position, ByteBuffer name, int name_position, long function_pointer);

	public static void glBindVertexArray(int array) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glBindVertexArray_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		nglBindVertexArray(array, function_pointer);
	}
	private static native void nglBindVertexArray(int array, long function_pointer);

	public static void glDeleteVertexArrays(IntBuffer arrays) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glDeleteVertexArrays_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		arrays = NondirectBufferWrapper.wrapDirect(arrays);
		nglDeleteVertexArrays((arrays.remaining()), arrays, arrays.position(), function_pointer);
	}
	private static native void nglDeleteVertexArrays(int n, IntBuffer arrays, int arrays_position, long function_pointer);

	public static void glGenVertexArrays(IntBuffer arrays) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glGenVertexArrays_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		IntBuffer arrays_saved = arrays;
		arrays = NondirectBufferWrapper.wrapNoCopyDirect(arrays);
		nglGenVertexArrays((arrays.remaining()), arrays, arrays.position(), function_pointer);
		NondirectBufferWrapper.copy(arrays, arrays_saved);
	}
	private static native void nglGenVertexArrays(int n, IntBuffer arrays, int arrays_position, long function_pointer);

	public static boolean glIsVertexArray(int array) {
		ContextCapabilities caps = GLContext.getCapabilities();
		long function_pointer = caps.GL30_glIsVertexArray_pointer;
		BufferChecks.checkFunctionAddress(function_pointer);
		boolean __result = nglIsVertexArray(array, function_pointer);
		return __result;
	}
	private static native boolean nglIsVertexArray(int array, long function_pointer);
}
