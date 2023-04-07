package com.dtyoung.spacecannons;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.dtyoung.spacecannons.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    double x = 0;
    double y = 0;
    double speedX = 10;
    double speedY = 10;
    double distance = 0;
    boolean hit = false;
    boolean fired = false;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setHasOptionsMenu(true);
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.Fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fired) {
                    fired = true;
                    binding.win.setVisibility(View.INVISIBLE);
                    if (binding.drawView.redTurn) {
                        x = binding.drawView.redX+2*binding.drawView.redPower *(float) Math.cos(binding.drawView.redAngle);
                        y = binding.drawView.redY+2*binding.drawView.redPower *(float) Math.sin(binding.drawView.redAngle);
                        speedX = Math.cos(binding.drawView.redAngle) * binding.drawView.redPower / 10;
                        speedY = Math.sin(binding.drawView.redAngle) * binding.drawView.redPower / 10;
                    } else {
                        x = binding.drawView.blueX+2*binding.drawView.bluePower *(float) Math.cos(binding.drawView.blueAngle);
                        y = binding.drawView.blueY+2*binding.drawView.bluePower *(float) Math.sin(binding.drawView.blueAngle);
                        speedX = Math.cos(binding.drawView.blueAngle) * binding.drawView.bluePower / 10;
                        speedY = Math.sin(binding.drawView.blueAngle) * binding.drawView.bluePower / 10;
                    }

                    hit = false;
                    final Handler handler = new Handler();
                    Runnable refresh = new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            if (binding.drawView.redTurn) {
                                binding.drawView.redProjectiles.add(new Integer[]{(int) x, (int) y});
                            } else {
                                binding.drawView.blueProjectiles.add(new Integer[]{(int) x, (int) y});
                            }
                            binding.drawView.invalidate();

                            for (Float[] P : binding.drawView.planets) {
                                distance = Math.sqrt(Math.pow(x - P[0], 2) + Math.pow(y - P[1], 2));
                                speedX -= Math.pow(P[2], 2) * (x - P[0]) / Math.pow(distance, 3);
                                speedY -= Math.pow(P[2], 2) * (y - P[1]) / Math.pow(distance, 3);
                                if (distance < P[2]) {
                                    hit = true;
                                }
                            }

                            x += speedX;
                            y += speedY;

                            if ((Math.abs(x - binding.drawView.blueX) < 20 && Math.abs(y - binding.drawView.blueY) < 20 && binding.drawView.redTurn) || (Math.abs(x - binding.drawView.redX) < 20 && Math.abs(y - binding.drawView.redY) < 20 && !binding.drawView.redTurn)) {
                                hit = true;
                                binding.win.setVisibility(View.VISIBLE);
                                binding.win.setTextColor(Color.WHITE);
                                if (binding.drawView.redTurn) {
                                    binding.win.setText("Red Wins!");
                                } else {
                                    binding.win.setText("Blue Wins!");
                                }
                            }
                            if (!hit) {
                                handler.postDelayed(this, 10);
                            }
                        }
                    };
                    handler.postDelayed(refresh, 10);
                }
            }
        });

        binding.EndTurn.setOnClickListener(view1 -> {
            fired = false;
            hit = true;
            binding.drawView.redTurn = !binding.drawView.redTurn;
            binding.drawView.invalidate();
        });

        binding.Teleport.setOnClickListener(view13 -> {
            if (!fired) {
                hit = true;
                binding.drawView.redProjectiles.clear();
                binding.drawView.blueProjectiles.clear();
                boolean tooClose = true;
                if (binding.drawView.redTurn) {
                    binding.drawView.redPower = 0;
                    binding.drawView.redAngle = 0;
                    while (tooClose) {
                        tooClose = false;
                        binding.drawView.redX = (float) (Math.random() * binding.drawView.getWidth());
                        binding.drawView.redY = (float) (Math.random() * binding.drawView.getHeight());
                        if (Math.abs(binding.drawView.redX - binding.drawView.blueX) < 500 || Math.abs(binding.drawView.redY - binding.drawView.blueY) < 500) {
                            tooClose = true;
                        }
                        for (Float[] P : binding.drawView.planets) {
                            if (Math.abs(binding.drawView.redX - P[0]) < P[2] || Math.abs(binding.drawView.redY - P[1]) < P[2]) {
                                tooClose = true;
                            }
                        }
                    }
                } else {
                    binding.drawView.bluePower = 0;
                    binding.drawView.blueAngle = 0;
                    while (tooClose) {
                        tooClose = false;
                        binding.drawView.blueX = (float) (Math.random() * binding.drawView.getWidth());
                        binding.drawView.blueY = (float) (Math.random() * binding.drawView.getHeight());
                        if (Math.abs(binding.drawView.redX - binding.drawView.blueX) < 500 || Math.abs(binding.drawView.redY - binding.drawView.blueY) < 500) {
                            tooClose = true;
                        }
                        for (Float[] P : binding.drawView.planets) {
                            if (Math.abs(binding.drawView.blueX - P[0]) < P[2] || Math.abs(binding.drawView.blueY - P[1]) < P[2]) {
                                tooClose = true;
                            }
                        }
                    }
                }
                binding.drawView.invalidate();
                binding.drawView.redTurn = !binding.drawView.redTurn;
            }
        });


        binding.drawView.setOnTouchListener((view12, motionEvent) -> {
            if (binding.drawView.redTurn) {
                float powerCheck = (float) (Math.sqrt(Math.pow(motionEvent.getX() - binding.drawView.redX, 2) + Math.pow(motionEvent.getY() - binding.drawView.redY, 2)) / 2);
                if (powerCheck <= 100) {
                    binding.drawView.redPower = powerCheck;
                    binding.drawView.redAngle = (float) Math.atan((motionEvent.getY() - binding.drawView.redY) / (motionEvent.getX() - binding.drawView.redX));
                    if (motionEvent.getX() < binding.drawView.redX) {
                        binding.drawView.redAngle += 3.1416;
                    }
                    binding.drawView.invalidate();
                }
            } else {
                float powerCheck = (float) (Math.sqrt(Math.pow(motionEvent.getX() - binding.drawView.blueX, 2) + Math.pow(motionEvent.getY() - binding.drawView.blueY, 2)) / 2);
                if (powerCheck <= 100) {
                    binding.drawView.bluePower = powerCheck;
                    binding.drawView.blueAngle = (float) Math.atan((motionEvent.getY() - binding.drawView.blueY) / (motionEvent.getX() - binding.drawView.blueX));
                    if (motionEvent.getX() < binding.drawView.blueX) {
                        binding.drawView.blueAngle += 3.1416;
                    }
                    binding.drawView.invalidate();
                }
            }

            view12.performClick();
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Menu item reset
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            binding.win.setVisibility(View.INVISIBLE);
            binding.drawView.SetRandom();
            binding.drawView.redProjectiles.clear();
            binding.drawView.blueProjectiles.clear();
            binding.drawView.invalidate();
            fired = false;
            binding.drawView.redPower = 0;
            binding.drawView.redAngle = 0;
            binding.drawView.bluePower = 0;
            binding.drawView.blueAngle = 0;
            hit = true;
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}