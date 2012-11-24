package argo.jdom;

import argo.jdom.JsonArrayNodeBuilder;
import argo.jdom.JsonFieldBuilder;
import argo.jdom.JsonListenerToJdomAdapter_Array;
import argo.jdom.JsonListenerToJdomAdapter_Field;
import argo.jdom.JsonListenerToJdomAdapter_NodeContainer;
import argo.jdom.JsonListenerToJdomAdapter_Object;
import argo.jdom.JsonNodeBuilder;
import argo.jdom.JsonNodeBuilders;
import argo.jdom.JsonObjectNodeBuilder;
import argo.jdom.JsonRootNode;
import argo.saj.JsonListener;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Stack;

@SideOnly(Side.CLIENT)
final class JsonListenerToJdomAdapter implements JsonListener {

   private final Stack field_74663_a = new Stack();
   private JsonNodeBuilder field_74662_b;


   JsonRootNode func_74660_a() {
      return (JsonRootNode)this.field_74662_b.func_74599_b();
   }

   public void func_74656_b() {}

   public void func_74657_c() {}

   public void func_74652_d() {
      JsonArrayNodeBuilder var1 = JsonNodeBuilders.func_74708_e();
      this.func_74661_a(var1);
      this.field_74663_a.push(new JsonListenerToJdomAdapter_Array(this, var1));
   }

   public void func_74655_e() {
      this.field_74663_a.pop();
   }

   public void func_74651_f() {
      JsonObjectNodeBuilder var1 = JsonNodeBuilders.func_74711_d();
      this.func_74661_a(var1);
      this.field_74663_a.push(new JsonListenerToJdomAdapter_Object(this, var1));
   }

   public void func_74653_g() {
      this.field_74663_a.pop();
   }

   public void func_74648_a(String p_74648_1_) {
      JsonFieldBuilder var2 = JsonFieldBuilder.func_74727_a().func_74726_a(JsonNodeBuilders.func_74710_b(p_74648_1_));
      ((JsonListenerToJdomAdapter_NodeContainer)this.field_74663_a.peek()).func_74716_a(var2);
      this.field_74663_a.push(new JsonListenerToJdomAdapter_Field(this, var2));
   }

   public void func_74658_h() {
      this.field_74663_a.pop();
   }

   public void func_74650_b(String p_74650_1_) {
      this.func_74659_b(JsonNodeBuilders.func_74712_a(p_74650_1_));
   }

   public void func_74654_i() {
      this.func_74659_b(JsonNodeBuilders.func_74713_b());
   }

   public void func_74647_c(String p_74647_1_) {
      this.func_74659_b(JsonNodeBuilders.func_74710_b(p_74647_1_));
   }

   public void func_74649_j() {
      this.func_74659_b(JsonNodeBuilders.func_74709_c());
   }

   public void func_74646_k() {
      this.func_74659_b(JsonNodeBuilders.func_74714_a());
   }

   private void func_74661_a(JsonNodeBuilder p_74661_1_) {
      if(this.field_74662_b == null) {
         this.field_74662_b = p_74661_1_;
      } else {
         this.func_74659_b(p_74661_1_);
      }

   }

   private void func_74659_b(JsonNodeBuilder p_74659_1_) {
      ((JsonListenerToJdomAdapter_NodeContainer)this.field_74663_a.peek()).func_74715_a(p_74659_1_);
   }
}
