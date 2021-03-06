package org.eclipse.xtend.lib;

import org.eclipse.xtend.lib.annotations.AccessorsProcessor;
import org.eclipse.xtend.lib.annotations.EqualsHashCodeProcessor;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructorProcessor;
import org.eclipse.xtend.lib.annotations.ToStringProcessor;
import org.eclipse.xtend.lib.macro.AbstractClassProcessor;
import org.eclipse.xtend.lib.macro.TransformationContext;
import org.eclipse.xtend.lib.macro.declaration.ClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.FieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableFieldDeclaration;
import org.eclipse.xtend.lib.macro.declaration.MutableMethodDeclaration;
import org.eclipse.xtend.lib.macro.declaration.Visibility;
import org.eclipse.xtend2.lib.StringConcatenationClient;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xbase.lib.Pure;
import org.eclipse.xtext.xbase.lib.StringExtensions;
import org.eclipse.xtext.xbase.lib.util.ToStringHelper;

/**
 * @since 2.7
 * @noextend
 * @noreference
 */
@Deprecated
@SuppressWarnings("all")
public class DataProcessor extends AbstractClassProcessor {
  /**
   * @since 2.7
   * @noextend
   * @noreference
   */
  @Deprecated
  public static class Util {
    @Extension
    private TransformationContext context;
    
    public Util(final TransformationContext context) {
      this.context = context;
    }
    
    public Iterable<? extends FieldDeclaration> getDataFields(final ClassDeclaration it) {
      final Function1<FieldDeclaration, Boolean> _function = new Function1<FieldDeclaration, Boolean>() {
        @Override
        public Boolean apply(final FieldDeclaration it) {
          return Boolean.valueOf(((!it.isStatic()) && Util.this.context.isThePrimaryGeneratedJavaElement(it)));
        }
      };
      return IterableExtensions.filter(it.getDeclaredFields(), _function);
    }
    
    public Iterable<? extends MutableFieldDeclaration> getDataFields(final MutableClassDeclaration it) {
      final Function1<MutableFieldDeclaration, Boolean> _function = new Function1<MutableFieldDeclaration, Boolean>() {
        @Override
        public Boolean apply(final MutableFieldDeclaration it) {
          return Boolean.valueOf(((!it.isStatic()) && Util.this.context.isThePrimaryGeneratedJavaElement(it)));
        }
      };
      return IterableExtensions.filter(it.getDeclaredFields(), _function);
    }
    
    public void addDataToString(final MutableClassDeclaration cls) {
      final Procedure1<MutableMethodDeclaration> _function = new Procedure1<MutableMethodDeclaration>() {
        @Override
        public void apply(final MutableMethodDeclaration it) {
          Util.this.context.setPrimarySourceElement(it, Util.this.context.getPrimarySourceElement(cls));
          it.setReturnType(Util.this.context.getString());
          it.addAnnotation(Util.this.context.newAnnotationReference(Override.class));
          it.addAnnotation(Util.this.context.newAnnotationReference(Pure.class));
          StringConcatenationClient _client = new StringConcatenationClient() {
            @Override
            protected void appendTo(StringConcatenationClient.TargetStringConcatenation _builder) {
              _builder.append("String result = new ");
              _builder.append(ToStringHelper.class);
              _builder.append("().toString(this);");
              _builder.newLineIfNotEmpty();
              _builder.append("return result;");
              _builder.newLine();
            }
          };
          it.setBody(_client);
        }
      };
      cls.addMethod("toString", _function);
    }
  }
  
  @Override
  public void doTransform(final MutableClassDeclaration it, @Extension final TransformationContext context) {
    @Extension
    final DataProcessor.Util util = new DataProcessor.Util(context);
    @Extension
    final AccessorsProcessor.Util getterUtil = new AccessorsProcessor.Util(context);
    @Extension
    final EqualsHashCodeProcessor.Util ehUtil = new EqualsHashCodeProcessor.Util(context);
    @Extension
    final ToStringProcessor.Util toStringUtil = new ToStringProcessor.Util(context);
    @Extension
    final FinalFieldsConstructorProcessor.Util requiredArgsUtil = new FinalFieldsConstructorProcessor.Util(context);
    final Procedure1<MutableFieldDeclaration> _function = new Procedure1<MutableFieldDeclaration>() {
      @Override
      public void apply(final MutableFieldDeclaration it) {
        it.setFinal(true);
      }
    };
    IterableExtensions.forEach(util.getDataFields(it), _function);
    boolean _needsFinalFieldConstructor = requiredArgsUtil.needsFinalFieldConstructor(it);
    if (_needsFinalFieldConstructor) {
      requiredArgsUtil.addFinalFieldsConstructor(it);
    }
    boolean _hasHashCode = ehUtil.hasHashCode(it);
    boolean _not = (!_hasHashCode);
    if (_not) {
      ehUtil.addHashCode(it, util.getDataFields(it), ehUtil.hasSuperHashCode(it));
    }
    boolean _hasEquals = ehUtil.hasEquals(it);
    boolean _not_1 = (!_hasEquals);
    if (_not_1) {
      ehUtil.addEquals(it, util.getDataFields(it), ehUtil.hasSuperEquals(it));
    }
    boolean _hasToString = toStringUtil.hasToString(it);
    boolean _not_2 = (!_hasToString);
    if (_not_2) {
      util.addDataToString(it);
    }
    final Procedure1<MutableFieldDeclaration> _function_1 = new Procedure1<MutableFieldDeclaration>() {
      @Override
      public void apply(final MutableFieldDeclaration it) {
        boolean _shouldAddGetter = getterUtil.shouldAddGetter(it);
        if (_shouldAddGetter) {
          getterUtil.addGetter(it, Visibility.PUBLIC);
        }
        String _firstLower = StringExtensions.toFirstLower(it.getSimpleName());
        String _plus = ("_" + _firstLower);
        it.setSimpleName(_plus);
      }
    };
    IterableExtensions.forEach(util.getDataFields(it), _function_1);
  }
}
