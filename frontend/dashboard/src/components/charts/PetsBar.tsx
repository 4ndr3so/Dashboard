import * as d3 from "d3";
import { useMemo, useState } from "react";

type Props = {
  data: { Dog?: number; Cat?: number; [k: string]: number | undefined };
  title?: string;
};

export default function PetsBar({ data, title = "Favorite pet type (2025)" }: Props) {
  const items = useMemo(
    () => ([
      { type: "Dog", value: data?.Dog ?? 0 },
      { type: "Cat", value: data?.Cat ?? 0 },
    ]),
    [data]
  );

  const [hover, setHover] = useState<string | null>(null);

  const width = 560;
  const height = 360;
  const margin = { top: 40, right: 16, bottom: 48, left: 56 };

  const x = d3.scaleBand()
    .domain(items.map(d => d.type))
    .range([margin.left, width - margin.right])
    .padding(0.35);

  const y = d3.scaleLinear()
    .domain([0, d3.max(items, d => d.value) || 0])
    .nice()
    .range([height - margin.bottom, margin.top]);

  return (
    <svg width={width} height={height} role="img" aria-label={title}>
      {/* Title */}
      <text x={width/2} y={22} textAnchor="middle" fontSize={16} fontWeight={600}>{title}</text>

      {/* Y grid + ticks */}
      <g transform={`translate(${margin.left},0)`} fontSize={11} fill="#666">
        {y.ticks(5).map(t => (
          <g key={t} transform={`translate(0,${y(t)})`}>
            <line x1={0} x2={width - margin.left - margin.right} stroke="#eee" />
            <text x={-8} dy="0.32em" textAnchor="end">{d3.format(",")(t)}</text>
          </g>
        ))}
      </g>

      {/* X labels */}
      <g transform={`translate(0,${height - margin.bottom})`} fontSize={12} fill="#444">
        {items.map(d => (
          <text key={d.type} x={(x(d.type) ?? 0) + (x.bandwidth()/2)} y={28} textAnchor="middle">
            {d.type}
          </text>
        ))}
      </g>

      {/* Bars */}
      {items.map(d => {
        const barX = x(d.type) ?? 0;
        const barY = y(d.value);
        const barH = y(0) - y(d.value);
        const active = hover === d.type;
        return (
          <g key={d.type}
             onMouseEnter={() => setHover(d.type)}
             onMouseLeave={() => setHover(null)}
             style={{ cursor: "pointer" }}>
            <rect
              x={barX}
              y={barY}
              width={x.bandwidth()}
              height={barH}
              rx={10}
              fill={active ? "#ff7f0e" : "#1f77b4"}
              style={{ transition: "fill 220ms, y 300ms, height 300ms" }}
            >
              <title>{`${d.type}: ${d3.format(",")(d.value)}`}</title>
            </rect>
            {/* Value label when hovered */}
            {active && (
              <text
                x={barX + x.bandwidth()/2}
                y={barY - 8}
                textAnchor="middle"
                fontWeight={700}
                fontSize={12}
              >
                {d3.format(",")(d.value)}
              </text>
            )}
          </g>
        );
      })}
    </svg>
  );
}
