import typing
from marshmallow import fields, utils, ValidationError

from MyProject.server.main import assert_id_exists
from MyProject.server.models import Customer, Post


class FieldExistingId(fields.Integer):
	def __init__(self, class_, required=False, **kwargs):
		self.class_ = class_
		super().__init__(strict=True, allow_none=True, required=required, **kwargs)

	def _validated(self, value):
		assert_id_exists(self.class_, value)
		return super()._validated(value)


class Custom(fields.String):
	def __init__(self, dump=None, load=None, **kwargs):
		self.serialize_method_name = None
		self._serialize_method = None
		self.serialize_func = None
		self.deserialize_method_name = None
		self._deserialize_method = None
		self.deserialize_func = None
		if dump is not None:
			if isinstance(dump, str):
				self.serialize_method_name = dump
			else:
				self.serialize_func = dump and utils.callable_or_raise(dump)
		if load is not None:
			if isinstance(load, str):
				self.deserialize_method_name = load
			else:
				self.deserialize_func = load and utils.callable_or_raise(load)
		super().__init__(**kwargs)

	def _serialize(self, value, attr, obj, **kwargs):
		if self._serialize_method is not None:
			return self._serialize_method(obj)
		if self.serialize_func is not None:
			return self._call_or_raise(self.serialize_func, obj, attr)
		return value

	def _deserialize(self, value, attr, data, **kwargs):
		if self._deserialize_method is not None:
			return self._deserialize_method(value)
		if self.deserialize_func:
			return self._call_or_raise(self.deserialize_func, value, attr)
		return value

	def _call_or_raise(self, func, value, attr):
		if len(utils.get_func_args(func)) > 1:
			if self.parent.context is None:
				msg = f"No context available for Function field {attr!r}"
				raise ValidationError(msg)
			return func(value, self.parent.context)
		else:
			return func(value)

	def _bind_to_schema(self, field_name, schema):
		if self.serialize_method_name:
			self._serialize_method = utils.callable_or_raise(
				getattr(schema, self.serialize_method_name)
			)

		if self.deserialize_method_name:
			self._deserialize_method = utils.callable_or_raise(
				getattr(schema, self.deserialize_method_name)
			)

		super()._bind_to_schema(field_name, schema)


def customer_id():
	return FieldExistingId(class_=Customer)


def post_id(**kwargs):
	return FieldExistingId(class_=Post)
