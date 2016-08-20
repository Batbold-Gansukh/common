package helpers.util

object FieldNameExtractor {
  def extract[T <: Product : Manifest] =
    implicitly[Manifest[T]].runtimeClass.getDeclaredFields
      .map(x ⇒ (Symbol(x.getName), x.getName)).filterNot(_._2.startsWith("$")).toMap
}



