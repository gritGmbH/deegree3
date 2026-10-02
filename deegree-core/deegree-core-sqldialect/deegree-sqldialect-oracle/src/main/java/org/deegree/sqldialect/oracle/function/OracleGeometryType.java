package org.deegree.sqldialect.oracle.function;

import org.deegree.sqldialect.SQLDialect;
import org.deegree.sqldialect.filter.expression.SQLColumn;
import org.deegree.sqldialect.filter.expression.SQLExpression;
import org.deegree.sqldialect.filter.expression.SQLOperationBuilder;
import org.deegree.sqldialect.filter.function.SQLFunctionProvider;
import org.deegree.workspace.Workspace;

import java.util.List;

import static java.sql.Types.VARCHAR;

public class OracleGeometryType implements SQLFunctionProvider {

	private static final String NAME = "geometryType";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public SQLExpression toProtoSQL(List<SQLExpression> args, SQLDialect dialect) {
		if (args.size() != 1 || !(args.get(0) instanceof SQLColumn)) {
			throw new IllegalArgumentException(
					"Unable to map function '" + NAME + "' to SQL. Expected a single column argument.");
		}
		SQLColumn column = (SQLColumn) args.get(0);

		SQLOperationBuilder builder = new SQLOperationBuilder(VARCHAR);

		builder.add("CASE MOD( ");
		builder.add(column.getSQL().append(".SDO_GTYPE").toString());
		builder.add(
				" , 100) WHEN 1 THEN 'Point' WHEN 2 THEN 'Curve' WHEN 3 THEN 'Surface' WHEN 4 THEN 'MultiGeometry' WHEN 5 THEN 'MultiPoint' WHEN 6 THEN 'MultiCurve' WHEN 7 THEN 'MultiSurface' WHEN 8 THEN 'Solid' WHEN 9 THEN 'MultiSolid' ELSE 'Geometry' END");

		return builder.toOperation();
	}

	@Override
	public void init(Workspace ws) {
		// nothing to do
	}

	@Override
	public void destroy() {
		// nothing to do
	}

}