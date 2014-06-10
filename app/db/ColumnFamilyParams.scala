package db

import com.google.common.collect.ImmutableMap

/**
 * Created with IntelliJ IDEA.
 * User: bangnv
 * Date: 8/30/13
 * Time: 11:08 AM
 * To change this template use File | Settings | File Templates.
 */
object ColumnFamilyParams {


  val userOptions: ImmutableMap[String, Object] = ImmutableMap.builder[String, Object]()
    .put("default_validation_class", "UTF8Type")
    .put("column_metadata", ImmutableMap.builder[String, Object]().
    put("password", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("created", ImmutableMap.builder[String, Object]().put("validation_class", "DateType").build()).
    build()).
    build();


/*  val profileOptions: java.util.Map[String, Object] = ImmutableMap.builder[String, Object]()
    .put("default_validation_class", "BytesType")
    .put("column_metadata", ImmutableMap.builder[String, Object]()
    .put("firstName", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type"))
    .put("lastName", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type"))
    .put("email", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").put("index_type","KEYS").build())
    .put("phone", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").put("index_type","KEYS").build())
    .build()).build()*/

  val profileOptions: ImmutableMap[String, Object] = ImmutableMap.builder[String, Object]()
    .put("default_validation_class", "UTF8Type")
    .put("column_metadata", ImmutableMap.builder[String, Object]().
    put("email", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").put("index_type", "KEYS").build()).
    put("phone", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").put("index_type", "KEYS").build()).
    put("firstName", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("lastName", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("address", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("birthday", ImmutableMap.builder[String, Object]().put("validation_class", "DateType").build()).
    put("created", ImmutableMap.builder[String, Object]().put("validation_class", "DateType").build())
    .build()).
    build();


  val courseOptions: ImmutableMap[String, Object] = ImmutableMap.builder[String, Object]()
    .put("default_validation_class", "UTF8Type")
    .put("column_metadata", ImmutableMap.builder[String, Object]().
    put("code", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("title", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("isOpen", ImmutableMap.builder[String, Object]().put("validation_class", "BooleanType").build()).
    put("teacher1", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("teacher2", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("comment", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("start", ImmutableMap.builder[String, Object]().put("validation_class", "DateType").build()).
    put("finish", ImmutableMap.builder[String, Object]().put("validation_class", "DateType").build()).
    put("days", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("hours", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    put("price", ImmutableMap.builder[String, Object]().put("validation_class", "IntegerType").build()).
    put("discount", ImmutableMap.builder[String, Object]().put("validation_class", "DoubleType").build()).
    put("room", ImmutableMap.builder[String, Object]().put("validation_class", "UTF8Type").build()).
    build()).
    build();


}
